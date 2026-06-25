package com.benevenuto.queue_master.application.printing_details;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.benevenuto.queue_master.domain.common.enums.OrderStatus;
import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.printing_details.repository.IPrintingDetailsRepository;

import jakarta.persistence.EntityNotFoundException;

class DeletePrintingOrderUseCaseTest {

    @Mock
    private IPrintingDetailsRepository printingRepository;

    private DeletePrintingOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new DeletePrintingOrderUseCase(printingRepository);
    }

    @Test
    void shouldThrowWhenOrderDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(printingRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDeleteAndReturnTheFullDeletedOrder() {
        UUID id = UUID.randomUUID();
        PrintingDetails order = PrintingDetails.builder()
                .id(id).workOrderNumber("WO-1").operatorNumber("1003")
                .printText("text").quantity(10).isUrgent(false).reason("reason")
                .status(OrderStatus.finished)
                .build();
        when(printingRepository.findById(id)).thenReturn(Optional.of(order));

        PrintingDetails result = useCase.execute(id);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.finished);
        assertThat(result.getOperatorNumber()).isEqualTo("1003");
        assertThat(result.getId()).isEqualTo(id);
        verify(printingRepository).deleteById(id);
    }
}
