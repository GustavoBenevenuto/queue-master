package com.benevenuto.queue_master.application.wire_cutting_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.DTO.wire_cutting_details.WireCuttingOrderRequestDTO;
import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateWireCuttingOrderUseCase {

    private final IWireCuttingDetailsRepository wireRepository;

    @Transactional
    public List<OrderDataNotificationDTO> execute(WireCuttingOrderRequestDTO dto) {
        List<OrderDataNotificationDTO> notifications = new ArrayList<>();

        Optional.ofNullable(dto.getItems()).ifPresent(list -> 
            list.forEach(item -> {
                wireRepository.save(WireCuttingDetails.builder()
                    .id(UUID.randomUUID())
                    .pwNumber(dto.getPwNumber())
                    .operatorNumber(dto.getOperatorNumber())
                    .wireName(item.getWireName())
                    .quantity(item.getQuantity())
                    .isUrgent(item.getIsUrgent())
                    .lengthMm(item.getLengthMm())
                    .reason(item.getReason())
                    .status(OrderStatus.pending)
                    .build());
                
                notifications.add(new OrderDataNotificationDTO(
                    RequestType.wire_cutting, 
                    OrderStatus.pending, 
                    dto.getOperatorNumber()
                ));
            }));

        return notifications;
    }
}