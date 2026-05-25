package com.benevenuto.queue_master.presentation.printing.websocket;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersUseCase;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.presentation.interfaces.IQueueEventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PrintingQueueEventPublisher implements IQueueEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final GetPrintingOrdersUseCase getPrintingOrdersUseCase;
    private final GetPrintingOrdersByOperatorUseCase getPrintingByOperatorUseCase;

    @Override
    public void publishQueueUpdate(OrderStatus status, String operatorNumber) {
        // 1. Atualiza a fila GERAL de impressão
        List<?> updatedGeneralQueue = getPrintingOrdersUseCase.execute();
        messagingTemplate.convertAndSend("/topic/printing", updatedGeneralQueue);

        // 2. Atualiza a fila do OPERADOR específico para impressão
        if (operatorNumber != null && !operatorNumber.isBlank()) {
            List<?> updatedOperatorQueue = getPrintingByOperatorUseCase.execute(operatorNumber);
            messagingTemplate.convertAndSend("/topic/printing/operator/" + operatorNumber, updatedOperatorQueue);
        }
    }
}