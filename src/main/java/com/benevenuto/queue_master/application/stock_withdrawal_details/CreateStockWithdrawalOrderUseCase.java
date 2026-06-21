package com.benevenuto.queue_master.application.stock_withdrawal_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.presentation.common.dto.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.stock_withdrawal_details.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.domain.stock_withdrawal_details.repository.IStockWithdrawalDetailsRepository;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateStockWithdrawalOrderUseCase {

    private final IStockWithdrawalDetailsRepository stockRepository;

    @Transactional
    public List<OrderDataNotificationDTO> execute(List<StockWithdrawalDetails> dto) {
        List<OrderDataNotificationDTO> notifications = new ArrayList<>();

        Optional.ofNullable(dto).ifPresent(list -> 
            list.forEach(item -> {
                stockRepository.save(StockWithdrawalDetails.builder()
                    .id(UUID.randomUUID())
                    .workOrderNumber(item.getWorkOrderNumber())
                    .operatorNumber(item.getOperatorNumber())
                    .itemName(item.getItemName())
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