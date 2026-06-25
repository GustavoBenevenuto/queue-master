package com.benevenuto.queue_master.application.printing_details;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.benevenuto.queue_master.domain.common.enums.OrderStatus;
import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.printing_details.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.presentation.common.dto.OrderDataNotificationDTO;
import com.benevenuto.queue_master.presentation.printing.dto.PrintingOrderRequestDTO;

class CreatePrintingOrderUseCaseTest {

    @Mock
    private IPrintingDetailsRepository printingRepository;

    private CreatePrintingOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreatePrintingOrderUseCase(printingRepository);
    }

    @Test
    void shouldCreateEveryOrderAlwaysAsPendingRegardlessOfInput() {
        PrintingOrderRequestDTO item1 = PrintingOrderRequestDTO.builder()
                .workOrderNumber("WO-1").operatorNumber("1001").quantity(10)
                .isUrgent(false).reason("reason 1").printText("text 1").build();
        PrintingOrderRequestDTO item2 = PrintingOrderRequestDTO.builder()
                .workOrderNumber("WO-2").operatorNumber("1002").quantity(5)
                .isUrgent(true).reason("reason 2").printText("text 2").build();

        List<OrderDataNotificationDTO> notifications = useCase.execute(List.of(item1, item2));

        assertThat(notifications).hasSize(2);
        assertThat(notifications).allSatisfy(n -> assertThat(n.status()).isEqualTo(OrderStatus.pending));
        assertThat(notifications).extracting(OrderDataNotificationDTO::operatorNumber)
                .containsExactly("1001", "1002");

        ArgumentCaptor<PrintingDetails> captor = ArgumentCaptor.forClass(PrintingDetails.class);
        verify(printingRepository, times(2)).save(captor.capture());

        assertThat(captor.getAllValues()).allSatisfy(saved -> {
            assertThat(saved.getStatus()).isEqualTo(OrderStatus.pending);
            assertThat(saved.getId()).isNotNull();
        });
    }

    @Test
    void shouldNotSaveAnythingWhenRequestListIsEmpty() {
        List<OrderDataNotificationDTO> notifications = useCase.execute(List.of());

        assertThat(notifications).isEmpty();
        verify(printingRepository, never()).save(any());
    }

    @Test
    void shouldNotSaveAnythingWhenRequestListIsNull() {
        List<OrderDataNotificationDTO> notifications = useCase.execute(null);

        assertThat(notifications).isEmpty();
        verify(printingRepository, never()).save(any());
    }
}
