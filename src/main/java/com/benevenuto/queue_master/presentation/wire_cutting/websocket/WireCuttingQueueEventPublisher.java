package com.benevenuto.queue_master.presentation.wire_cutting.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.common.enums.QueueEventType;
import com.benevenuto.queue_master.domain.wire_cutting_details.entity.WireCuttingDetails;
import com.benevenuto.queue_master.presentation.common.dto.QueueDeltaEventDTO;
import com.benevenuto.queue_master.presentation.interfaces.IQueueEventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WireCuttingQueueEventPublisher implements IQueueEventPublisher<WireCuttingDetails> {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publishQueueUpdate(QueueEventType type, WireCuttingDetails order) {
        QueueDeltaEventDTO<WireCuttingDetails> event = new QueueDeltaEventDTO<>(type, order);

        // 1. Avisa o painel geral: o objeto completo do item alterado, nunca a fila inteira
        messagingTemplate.convertAndSend("/topic/wire-cutting", event);

        // 2. Avisa o feed privado do operador dono da ordem
        String operatorNumber = order.getOperatorNumber();
        if (operatorNumber != null && !operatorNumber.isBlank()) {
            messagingTemplate.convertAndSend("/topic/wire-cutting/operator/" + operatorNumber, event);
        }
    }
}
