package com.benevenuto.queue_master.presentation.wire_cutting.websocket;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersUseCase;
import com.benevenuto.queue_master.domain.order_queue.enums.OrderStatus;
import com.benevenuto.queue_master.presentation.interfaces.IQueueEventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WireCuttingQueueEventPublisher implements IQueueEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final GetWireCuttingOrdersUseCase getWireCuttingOrdersUseCase;
    private final GetWireCuttingOrdersByOperatorUseCase getWireCuttingByOperatorUseCase;

    @Override
    public void publishQueueUpdate(OrderStatus status, String operatorNumber) {
        // 1. Atualiza a fila GERAL de corte de cabos
        List<?> updatedGeneralQueue = getWireCuttingOrdersUseCase.execute();
        messagingTemplate.convertAndSend("/topic/wire-cutting", updatedGeneralQueue);

        // 2. Atualiza a fila do OPERADOR específico para corte de cabos
        if (operatorNumber != null && !operatorNumber.isBlank()) {
            List<?> updatedOperatorQueue = getWireCuttingByOperatorUseCase.execute(operatorNumber);
            messagingTemplate.convertAndSend("/topic/wire-cutting/operator/" + operatorNumber, updatedOperatorQueue);
        }
    }
}