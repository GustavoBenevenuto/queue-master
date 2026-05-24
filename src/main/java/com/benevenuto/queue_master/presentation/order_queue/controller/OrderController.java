package com.benevenuto.queue_master.presentation.order_queue.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.DTO.OrderRequestDTO;
import com.benevenuto.queue_master.DTO.OrderResponseDTO;
import com.benevenuto.queue_master.application.order_queue.CreateOrderUseCase;
import com.benevenuto.queue_master.application.order_queue.DeleteOrderUseCase;
import com.benevenuto.queue_master.application.order_queue.GetQueueByStationUseCase;
import com.benevenuto.queue_master.application.order_queue.UpdateOrderStatusUseCase;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;
import com.benevenuto.queue_master.presentation.websocket.QueueEventPublisher;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetQueueByStationUseCase getQueueByStationUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final QueueEventPublisher queueEventPublisher;

    // Construtor único atualizado para incluir o publicador de WebSocket
    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetQueueByStationUseCase getQueueByStationUseCase,
            UpdateOrderStatusUseCase updateOrderStatusUseCase,
            DeleteOrderUseCase deleteOrderUseCase,
            QueueEventPublisher queueEventPublisher
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getQueueByStationUseCase = getQueueByStationUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.queueEventPublisher = queueEventPublisher;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody OrderRequestDTO dto) {
        // 1. Executa a criação e colhe a lista de notificações geradas pelo UseCase
        List<OrderDataNotificationDTO> createdOrders = createOrderUseCase.execute(dto);

        // 2. Transmite uma atualização de WebSocket para cada tipo distinto criado
        createdOrders.stream()
            .distinct() // Evita spammer e disparar múltiplos sinais idênticos na rede para o mesmo tipo
            .forEach(notification -> 
                queueEventPublisher.publishQueueUpdate(notification.type(), notification.status())
            );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/queue/{type}")
    public ResponseEntity<List<OrderResponseDTO>> listByStation(
            @PathVariable RequestType type,
            @RequestParam(defaultValue = "pending") OrderStatus status) {
        return ResponseEntity.ok(getQueueByStationUseCase.execute(type, status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        
        // 1. Executa a atualização e colhe o estado antigo da ordem via UseCase
        OrderDataNotificationDTO oldOrderData = updateOrderStatusUseCase.execute(id, status);

        // 2. Transmite a atualização para os painéis afetados
        // Notifica a fila antiga (para tirar o card da tela) e a nova fila (para adicionar o card na tela correspondente)
        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), oldOrderData.status());
        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), status);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        // 1. Executa a deleção e colhe os dados da ordem que foi eliminada
        OrderDataNotificationDTO deletedOrderData = deleteOrderUseCase.execute(id);

        // 2. Transmite o sinal de WebSocket para remover o item da listagem da fábrica em tempo real
        queueEventPublisher.publishQueueUpdate(deletedOrderData.type(), deletedOrderData.status());

        return ResponseEntity.noContent().build();
    }
}