package com.benevenuto.queue_master.application.stock_withdrawal_details;

import java.util.UUID;

import com.benevenuto.queue_master.presentation.common.dto.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.stock_withdrawal_details.repository.IStockWithdrawalDetailsRepository;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateStockWithdrawalOrderStatusUseCase {
    
    private final IStockWithdrawalDetailsRepository stockRepository;

    @Transactional
    public OrderDataNotificationDTO execute(UUID id, OrderStatus newStatus) {
        return stockRepository.findById(id)
            .map(entity -> {
                OrderStatus oldStatus = entity.getStatus();
                entity.setStatus(newStatus);
                stockRepository.save(entity);
                return new OrderDataNotificationDTO(
                    oldStatus, 
                    entity.getOperatorNumber()
                );
            })
            .orElseThrow(() -> new EntityNotFoundException("Stock withdrawal order with ID " + id + " not found."));
    }
}