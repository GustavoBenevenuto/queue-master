package com.benevenuto.queue_master.application.wire_cutting_details;

import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteWireCuttingOrderUseCase {

    private final IWireCuttingDetailsRepository wireRepository;
    
    @Transactional
    public OrderDataNotificationDTO execute(UUID id) {
        return wireRepository.findById(id)
            .map(entity -> {
                wireRepository.deleteById(id);
                return new OrderDataNotificationDTO(
                    entity.getStatus(), 
                    entity.getOperatorNumber()
                );
            })
            .orElseThrow(() -> new EntityNotFoundException("Wire cutting order with ID " + id + " not found."));
    }
}