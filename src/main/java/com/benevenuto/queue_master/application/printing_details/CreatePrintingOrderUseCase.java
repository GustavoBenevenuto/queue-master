package com.benevenuto.queue_master.application.printing_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.presentation.common.dto.OrderDataNotificationDTO;
import com.benevenuto.queue_master.presentation.printing.dto.PrintingOrderRequestDTO;
import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.domain.order_queue.enums.OrderStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreatePrintingOrderUseCase {

    private final IPrintingDetailsRepository printingRepository;

    @Transactional
    public List<OrderDataNotificationDTO> execute(List<PrintingOrderRequestDTO> dto) {
        List<OrderDataNotificationDTO> notifications = new ArrayList<>();

        Optional.ofNullable(dto).ifPresent(list -> 
            list.forEach(item -> {
                printingRepository.save(PrintingDetails.builder()
                    .id(UUID.randomUUID())
                    .workOrderNumber(item.getWorkOrderNumber())
                    .operatorNumber(item.getOperatorNumber())
                    .printText(item.getPrintText())
                    .quantity(item.getQuantity())
                    .isUrgent(item.getIsUrgent())
                    .reason(item.getReason())
                    .status(OrderStatus.pending)
                    .build());
                
                notifications.add(new OrderDataNotificationDTO(
                    OrderStatus.pending, 
                    item.getOperatorNumber()
                ));
            }));

        return notifications;
    }
}