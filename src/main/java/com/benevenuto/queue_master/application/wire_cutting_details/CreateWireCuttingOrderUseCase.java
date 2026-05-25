package com.benevenuto.queue_master.application.wire_cutting_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateWireCuttingOrderUseCase {

    private final IWireCuttingDetailsRepository wireRepository;

    @Transactional
    public List<OrderDataNotificationDTO> execute(List<WireCuttingDetails> dto) {
        List<OrderDataNotificationDTO> notifications = new ArrayList<>();

        Optional.ofNullable(dto).ifPresent(list -> 
            list.forEach(item -> {
                wireRepository.save(WireCuttingDetails.builder()
                    .id(UUID.randomUUID())
                    .pwNumber(item.getPwNumber())
                    .operatorNumber(item.getOperatorNumber())
                    .wireName(item.getWireName())
                    .quantity(item.getQuantity())
                    .isUrgent(item.getIsUrgent())
                    .lengthMm(item.getLengthMm())
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