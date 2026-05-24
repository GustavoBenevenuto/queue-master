package com.benevenuto.queue_master.presentation.websocket;

import com.benevenuto.queue_master.DTO.OrderResponseDTO;
import com.benevenuto.queue_master.application.order_queue.GetQueueByStationUseCase;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueueEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final GetQueueByStationUseCase getQueueByStationUseCase;

    public QueueEventPublisher(SimpMessagingTemplate messagingTemplate, GetQueueByStationUseCase getQueueByStationUseCase) {
        this.messagingTemplate = messagingTemplate;
        this.getQueueByStationUseCase = getQueueByStationUseCase;
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
}