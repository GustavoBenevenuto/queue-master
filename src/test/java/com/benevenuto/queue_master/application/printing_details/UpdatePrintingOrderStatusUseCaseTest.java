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

class UpdatePrintingOrderStatusUseCaseTest {

    @Mock
    private IPrintingDetailsRepository printingRepository;

    private UpdatePrintingOrderStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UpdatePrintingOrderStatusUseCase(printingRepository);
    }

    @Test
    void shouldThrowWhenOrderDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(printingRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, OrderStatus.in_progress))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdateStatusAndReturnTheFullUpdatedOrder() {
        UUID id = UUID.randomUUID();
        PrintingDetails order = PrintingDetails.builder()
                .id(id).workOrderNumber("WO-1").operatorNumber("1003")
                .printText("text").quantity(10).isUrgent(false).reason("reason")
                .status(OrderStatus.pending)
                .build();
        when(printingRepository.findById(id)).thenReturn(Optional.of(order));
        when(printingRepository.save(order)).thenReturn(order);

        PrintingDetails result = useCase.execute(id, OrderStatus.in_progress);

        // The use case returns the full entity already updated - the publisher relies on this
        // to broadcast the complete object (not just id/status) and on getOperatorNumber() to
        // route to the order's own operator topic, regardless of who performed the request.
        assertThat(result.getStatus()).isEqualTo(OrderStatus.in_progress);
        assertThat(result.getOperatorNumber()).isEqualTo("1003");
        assertThat(result.getId()).isEqualTo(id);

        verify(printingRepository).save(order);
    }
}
