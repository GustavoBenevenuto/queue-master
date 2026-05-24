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
import com.benevenuto.queue_master.application.order_queue.GetOrdersByOperatorUseCase;
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
    private final GetOrdersByOperatorUseCase getOrdersByOperatorUseCase;

    // Construtor único atualizado para incluir o publicador de WebSocket
    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetQueueByStationUseCase getQueueByStationUseCase,
            UpdateOrderStatusUseCase updateOrderStatusUseCase,
            DeleteOrderUseCase deleteOrderUseCase,
            QueueEventPublisher queueEventPublisher,
            GetOrdersByOperatorUseCase getOrdersByOperatorUseCase
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getQueueByStationUseCase = getQueueByStationUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.queueEventPublisher = queueEventPublisher;
        this.getOrdersByOperatorUseCase = getOrdersByOperatorUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody OrderRequestDTO dto) {
        List<OrderDataNotificationDTO> createdOrders = createOrderUseCase.execute(dto);

        createdOrders.stream()
            .distinct()
            .forEach(notification -> 
                queueEventPublisher.publishQueueUpdate(notification.type(), notification.status())
            );

        // Notifica o app do operador sobre as novas ordens criadas
        queueEventPublisher.publishOperatorUpdate(dto.getOperatorNumber());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/queue/{type}")
    public ResponseEntity<List<OrderResponseDTO>> listByStation(
            @PathVariable RequestType type,
            @RequestParam(defaultValue = "pending") OrderStatus status) {
        return ResponseEntity.ok(getQueueByStationUseCase.execute(type, status));
    }
    
    @GetMapping("/queue/operator/{operatorNumber}")
    public ResponseEntity<List<OrderResponseDTO>> listByOperator(@PathVariable String operatorNumber) {
        return ResponseEntity.ok(getOrdersByOperatorUseCase.execute(operatorNumber));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        
        OrderDataNotificationDTO oldOrderData = updateOrderStatusUseCase.execute(id, status);

        // Atualiza os painéis gerais das estações na fábrica
        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), oldOrderData.status());
        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), status);

        // Atualiza a tela do operador em tempo real informando a mudança de status
        queueEventPublisher.publishOperatorUpdate(oldOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        OrderDataNotificationDTO deletedOrderData = deleteOrderUseCase.execute(id);

        // Remove do painel geral da estação correspondente
        queueEventPublisher.publishQueueUpdate(deletedOrderData.type(), deletedOrderData.status());

        // Remove em tempo real da tela do operador específico
        queueEventPublisher.publishOperatorUpdate(deletedOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }
}
