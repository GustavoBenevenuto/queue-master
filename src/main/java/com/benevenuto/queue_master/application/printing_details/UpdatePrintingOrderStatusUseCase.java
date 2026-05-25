package com.benevenuto.queue_master.application.printing_details;

import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdatePrintingOrderStatusUseCase {
    
    private final IPrintingDetailsRepository printingRepository;

    @Transactional
    public OrderDataNotificationDTO execute(UUID id, OrderStatus newStatus) {
        return printingRepository.findById(id)
            .map(entity -> {
                OrderStatus oldStatus = entity.getStatus();
                entity.setStatus(newStatus);
                printingRepository.save(entity);
                return new OrderDataNotificationDTO(
                    oldStatus, 
                    entity.getOperatorNumber()
                );
            })
            .orElseThrow(() -> new EntityNotFoundException("Printing order with ID " + id + " not found."));
    }
}
