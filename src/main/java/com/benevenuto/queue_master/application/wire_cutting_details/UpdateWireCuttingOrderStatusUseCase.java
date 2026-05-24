package com.benevenuto.queue_master.application.wire_cutting_details;

import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateWireCuttingOrderStatusUseCase {
    
    private final IWireCuttingDetailsRepository wireRepository;

    @Transactional
    public OrderDataNotificationDTO execute(UUID id, OrderStatus newStatus) {
        return wireRepository.findById(id)
            .map(entity -> {
                OrderStatus oldStatus = entity.getStatus();
                entity.setStatus(newStatus);
                wireRepository.save(entity);
                return new OrderDataNotificationDTO(
                    RequestType.wire_cutting, 
                    oldStatus, 
                    entity.getOperatorNumber()
                );
            })
            .orElseThrow(() -> new EntityNotFoundException("Wire cutting order with ID " + id + " not found."));
    }
}