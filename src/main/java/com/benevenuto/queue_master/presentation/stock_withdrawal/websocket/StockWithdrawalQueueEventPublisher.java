package com.benevenuto.queue_master.presentation.stock_withdrawal.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.common.enums.QueueEventType;
import com.benevenuto.queue_master.domain.stock_withdrawal_details.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.presentation.common.dto.QueueDeltaEventDTO;
import com.benevenuto.queue_master.presentation.interfaces.IQueueEventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockWithdrawalQueueEventPublisher implements IQueueEventPublisher<StockWithdrawalDetails> {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publishQueueUpdate(QueueEventType type, StockWithdrawalDetails order) {
        QueueDeltaEventDTO<StockWithdrawalDetails> event = new QueueDeltaEventDTO<>(type, order);

        // 1. Avisa o painel geral: o objeto completo do item alterado, nunca a fila inteira
        messagingTemplate.convertAndSend("/topic/stock-withdrawal", event);

        // 2. Avisa o feed privado do operador dono da ordem
        String operatorNumber = order.getOperatorNumber();
        if (operatorNumber != null && !operatorNumber.isBlank()) {
            messagingTemplate.convertAndSend("/topic/stock-withdrawal/operator/" + operatorNumber, event);
        }
    }
}
