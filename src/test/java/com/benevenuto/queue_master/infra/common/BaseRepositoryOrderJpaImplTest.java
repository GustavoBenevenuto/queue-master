package com.benevenuto.queue_master.infra.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.benevenuto.queue_master.domain.common.enums.OrderStatus;
import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.printing_details.repository.IPrintingDetailsRepository;

/**
 * Teste de regressão para o bug do Query by Example: o probe criado via reflection herda os
 * valores default declarados na entidade (isUrgent = false, status = OrderStatus.pending).
 * Como o QBE só ignora valores nulos, sem um ExampleMatcher explícito esses defaults entrariam
 * como filtro e quebrariam findByOperatorNumber/findByWorkOrderNumber para qualquer ordem que
 * não estivesse "pending" e "não urgente".
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BaseRepositoryOrderJpaImplTest {

    @Autowired
    private IPrintingDetailsRepository printingDetailsRepository;

    @BeforeEach
    void setUp() {
        printingDetailsRepository.save(buildOrder("WO-URGENT-FINISHED", "9001", true, OrderStatus.finished));
        printingDetailsRepository.save(buildOrder("WO-CALM-PROGRESS", "9001", false, OrderStatus.in_progress));
        printingDetailsRepository.save(buildOrder("WO-OTHER-OPERATOR", "9002", false, OrderStatus.pending));
    }

    @AfterEach
    void tearDown() {
        printingDetailsRepository.findByOperatorNumber("9001")
                .forEach(order -> printingDetailsRepository.deleteById(order.getId()));
        printingDetailsRepository.findByOperatorNumber("9002")
                .forEach(order -> printingDetailsRepository.deleteById(order.getId()));
    }

    @Test
    void shouldFindAllOrdersByOperatorRegardlessOfStatusOrIsUrgent() {
        List<PrintingDetails> result = printingDetailsRepository.findByOperatorNumber("9001");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(PrintingDetails::getWorkOrderNumber)
                .containsExactlyInAnyOrder("WO-URGENT-FINISHED", "WO-CALM-PROGRESS");
    }

    @Test
    void shouldFindOrderByWorkOrderNumberRegardlessOfStatusOrIsUrgent() {
        List<PrintingDetails> result = printingDetailsRepository.findByWorkOrderNumber("WO-URGENT-FINISHED");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.finished);
        assertThat(result.get(0).getIsUrgent()).isTrue();
    }

    private PrintingDetails buildOrder(String workOrderNumber, String operatorNumber, boolean urgent, OrderStatus status) {
        return PrintingDetails.builder()
                .id(UUID.randomUUID())
                .workOrderNumber(workOrderNumber)
                .operatorNumber(operatorNumber)
                .printText("text")
                .quantity(10)
                .isUrgent(urgent)
                .reason("reason")
                .status(status)
                .build();
    }
}
