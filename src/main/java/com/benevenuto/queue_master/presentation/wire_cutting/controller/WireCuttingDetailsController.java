package com.benevenuto.queue_master.presentation.wire_cutting.controller;

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

import com.benevenuto.queue_master.application.wire_cutting_details.CreateWireCuttingOrderUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.DeleteWireCuttingOrderUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.UpdateWireCuttingOrderStatusUseCase;
import com.benevenuto.queue_master.domain.wire_cutting_details.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;
import com.benevenuto.queue_master.domain.common.enums.QueueEventType;
import com.benevenuto.queue_master.presentation.wire_cutting.websocket.WireCuttingQueueEventPublisher;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders/wire-cutting")
@RequiredArgsConstructor
public class WireCuttingDetailsController {

    private final CreateWireCuttingOrderUseCase createUseCase;
    private final DeleteWireCuttingOrderUseCase deleteUseCase;
    private final GetWireCuttingOrdersByOperatorUseCase getByOperatorUseCase;
    private final GetWireCuttingOrdersUseCase getWireCuttingOrdersUseCase;
    private final UpdateWireCuttingOrderStatusUseCase updateStatusUseCase;
    private final WireCuttingQueueEventPublisher queueEventPublisher;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody List<WireCuttingDetails> dto) {
        List<WireCuttingDetails> createdOrders = createUseCase.execute(dto);

        // Cada ordem criada gera seu próprio evento de delta no WebSocket, com o objeto completo
        createdOrders.forEach(order ->
            queueEventPublisher.publishQueueUpdate(QueueEventType.ORDER_CREATED, order)
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/operator/{operatorNumber}")
    public ResponseEntity<List<WireCuttingDetails>> listByOperator(@PathVariable String operatorNumber) {
        return ResponseEntity.ok(getByOperatorUseCase.execute(operatorNumber));
    }

    @GetMapping()
    public ResponseEntity<List<WireCuttingDetails>> listAll() {
        return ResponseEntity.ok(getWireCuttingOrdersUseCase.execute());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {

        WireCuttingDetails updatedOrder = updateStatusUseCase.execute(id, status);

        queueEventPublisher.publishQueueUpdate(QueueEventType.STATUS_CHANGED, updatedOrder);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        WireCuttingDetails deletedOrder = deleteUseCase.execute(id);

        queueEventPublisher.publishQueueUpdate(QueueEventType.ORDER_DELETED, deletedOrder);

        return ResponseEntity.noContent().build();
    }
}
