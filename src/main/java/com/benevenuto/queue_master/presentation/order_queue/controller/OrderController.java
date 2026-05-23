package com.benevenuto.queue_master.presentation.order_queue.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.benevenuto.queue_master.DTO.OrderRequestDTO;
import com.benevenuto.queue_master.DTO.OrderResponseDTO;
import com.benevenuto.queue_master.application.order_queue.CreateOrderUseCase;
import com.benevenuto.queue_master.application.order_queue.DeleteOrderUseCase;
import com.benevenuto.queue_master.application.order_queue.GetQueueByStationUseCase;
import com.benevenuto.queue_master.application.order_queue.UpdateOrderStatusUseCase;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetQueueByStationUseCase getQueueByStationUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;

    // Construtor único para injeção de todas as dependências
    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetQueueByStationUseCase getQueueByStationUseCase,
            UpdateOrderStatusUseCase updateOrderStatusUseCase,
            DeleteOrderUseCase deleteOrderUseCase
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getQueueByStationUseCase = getQueueByStationUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody OrderRequestDTO dto) {
        createOrderUseCase.execute(dto);
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
        updateOrderStatusUseCase.execute(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteOrderUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}