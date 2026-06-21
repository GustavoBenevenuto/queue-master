package com.benevenuto.queue_master.presentation.stock_withdrawal.websocket;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersUseCase;
import com.benevenuto.queue_master.domain.order_queue.enums.OrderStatus;
import com.benevenuto.queue_master.presentation.interfaces.IQueueEventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockWithdrawalQueueEventPublisher implements IQueueEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final GetStockWithdrawalOrdersUseCase getStockWithdrawalOrdersUseCase;
    private final GetStockWithdrawalOrdersByOperatorUseCase getStockWithdrawalByOperatorUseCase;

    @Override
    public void publishQueueUpdate(OrderStatus status, String operatorNumber) {
        // 1. Atualiza a fila GERAL de estoque
        List<?> updatedGeneralQueue = getStockWithdrawalOrdersUseCase.execute();
        messagingTemplate.convertAndSend("/topic/stock-withdrawal", updatedGeneralQueue);

        // 2. Atualiza a fila do OPERADOR específico para estoque
        if (operatorNumber != null && !operatorNumber.isBlank()) {
            List<?> updatedOperatorQueue = getStockWithdrawalByOperatorUseCase.execute(operatorNumber);
            messagingTemplate.convertAndSend("/topic/stock-withdrawal/operator/" + operatorNumber, updatedOperatorQueue);
        }
    }
}