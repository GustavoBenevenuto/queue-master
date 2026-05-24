package com.benevenuto.queue_master.application.stock_withdrawal_details;

import java.util.UUID;
import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.repository.IStockWithdrawalDetailsRepository;
import com.benevenuto.queue_master.enums.RequestType;
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
                    RequestType.stock_withdrawal, 
                    entity.getStatus(), 
                    entity.getOperatorNumber()
                );
            })
            .orElseThrow(() -> new EntityNotFoundException("Stock withdrawal order with ID " + id + " not found."));
    }
}