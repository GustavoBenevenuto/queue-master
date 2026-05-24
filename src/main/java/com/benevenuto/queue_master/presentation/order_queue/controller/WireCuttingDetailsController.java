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
import com.benevenuto.queue_master.DTO.wire_cutting_details.WireCuttingOrderRequestDTO;
import com.benevenuto.queue_master.application.wire_cutting_details.CreateWireCuttingOrderUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.DeleteWireCuttingOrderUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.UpdateWireCuttingOrderStatusUseCase;
import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.presentation.websocket.QueueEventPublisher;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders/wire-cutting")
@RequiredArgsConstructor
public class WireCuttingDetailsController {

    private final CreateWireCuttingOrderUseCase createUseCase;
    private final DeleteWireCuttingOrderUseCase deleteUseCase;
    private final GetWireCuttingOrdersByOperatorUseCase getByOperatorUseCase;
    private final UpdateWireCuttingOrderStatusUseCase updateStatusUseCase;
    private final QueueEventPublisher queueEventPublisher;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody WireCuttingOrderRequestDTO dto) {
        List<OrderDataNotificationDTO> createdOrders = createUseCase.execute(dto);

        createdOrders.stream()
            .distinct()
            .forEach(notification -> 
                queueEventPublisher.publishQueueUpdate(notification.type(), notification.status())
            );

        queueEventPublisher.publishOperatorUpdate(dto.getOperatorNumber());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/operator/{operatorNumber}")
    public ResponseEntity<List<WireCuttingDetails>> listByOperator(@PathVariable String operatorNumber) {
        return ResponseEntity.ok(getByOperatorUseCase.execute(operatorNumber));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        
        OrderDataNotificationDTO oldOrderData = updateStatusUseCase.execute(id, status);

        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), oldOrderData.status());
        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), status);
        queueEventPublisher.publishOperatorUpdate(oldOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        OrderDataNotificationDTO deletedOrderData = deleteUseCase.execute(id);

        queueEventPublisher.publishQueueUpdate(deletedOrderData.type(), deletedOrderData.status());
        queueEventPublisher.publishOperatorUpdate(deletedOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }
}