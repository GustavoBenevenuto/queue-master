package com.benevenuto.queue_master.presentation.websocket;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.DTO.OrderResponseDTO;
import com.benevenuto.queue_master.application.order_queue.GetOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.order_queue.GetQueueByStationUseCase;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

@Component
public class QueueEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final GetQueueByStationUseCase getQueueByStationUseCase;
    private final GetOrdersByOperatorUseCase getOrdersByOperatorUseCase;

    public QueueEventPublisher(SimpMessagingTemplate messagingTemplate, GetQueueByStationUseCase getQueueByStationUseCase, GetOrdersByOperatorUseCase getOrdersByOperatorUseCase) {
        this.messagingTemplate = messagingTemplate;
        this.getQueueByStationUseCase = getQueueByStationUseCase;
        this.getOrdersByOperatorUseCase = getOrdersByOperatorUseCase;
    }

    /**
     * Chame este método sempre que o status de uma ordem mudar ou uma nova ordem entrar.
     */
    public void publishQueueUpdate(RequestType type, OrderStatus status) {
        // 1. Busca a listagem atualizada e priorizada usando o seu UseCase
        List<OrderResponseDTO> updatedQueue = getQueueByStationUseCase.execute(type, status);

        // 2. Envia a nova lista dinamicamente para o tópico daquela estação/status específico
        // Exemplo de rota: /topic/queue/wire_cutting/pending
        String destination = String.format("/topic/queue/%s/%s", type.name(), status.name());
        
        messagingTemplate.convertAndSend(destination, updatedQueue);
    }
    
    public void publishOperatorUpdate(String operatorNumber) {
        // 1. Busca a lista atualizada de solicitações daquele operador específico
        List<OrderResponseDTO> updatedOperatorOrders = getOrdersByOperatorUseCase.execute(operatorNumber);
        
        // 2. Envia para o tópico exclusivo dele. Ex: /topic/operator/OP-TESTE
        String destination = "/topic/operator/" + operatorNumber;
        messagingTemplate.convertAndSend(destination, updatedOperatorOrders);
    }
}