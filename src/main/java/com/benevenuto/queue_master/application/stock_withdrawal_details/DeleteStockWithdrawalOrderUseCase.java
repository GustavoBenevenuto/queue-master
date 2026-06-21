package com.benevenuto.queue_master.application.stock_withdrawal_details;

import java.util.UUID;

import com.benevenuto.queue_master.presentation.common.dto.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.stock_withdrawal_details.repository.IStockWithdrawalDetailsRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteStockWithdrawalOrderUseCase {

    private final IStockWithdrawalDetailsRepository stockRepository;
    
    @Transactional
    public OrderDataNotificationDTO execute(UUID id) {
        return stockRepository.findById(id)
            .map(entity -> {
                stockRepository.deleteById(id);
                return new OrderDataNotificationDTO(
                    entity.getStatus(), 
                    entity.getOperatorNumber()
                );
            })
            .orElseThrow(() -> new EntityNotFoundException("Stock withdrawal order with ID " + id + " not found."));
    }
}