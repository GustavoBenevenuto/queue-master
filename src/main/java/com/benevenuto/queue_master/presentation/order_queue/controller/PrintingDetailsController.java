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
import com.benevenuto.queue_master.DTO.pritting_details.PrintingOrderRequestDTO;
import com.benevenuto.queue_master.application.printing_details.CreatePrintingOrderUseCase;
import com.benevenuto.queue_master.application.printing_details.DeletePrintingOrderUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersUseCase;
import com.benevenuto.queue_master.application.printing_details.UpdatePrintingOrderStatusUseCase;
import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.presentation.websocket.QueueEventPublisher;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders/printing")
@RequiredArgsConstructor
public class PrintingDetailsController {

    private final CreatePrintingOrderUseCase createUseCase;
    private final DeletePrintingOrderUseCase deleteUseCase;
    private final GetPrintingOrdersByOperatorUseCase getByOperatorUseCase;
    private final GetPrintingOrdersUseCase getPrintingOrdersUseCase;
    private final UpdatePrintingOrderStatusUseCase updateStatusUseCase;
    private final QueueEventPublisher queueEventPublisher;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody List<PrintingOrderRequestDTO> dto, @RequestParam String opNumber) {
        List<OrderDataNotificationDTO> createdOrders = createUseCase.execute(dto);

        createdOrders.stream()
            .distinct()
            .forEach(notification -> 
                queueEventPublisher.publishQueueUpdate(notification.type(), notification.status(), opNumber)
            );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/operator/{operatorNumber}")
    public ResponseEntity<List<PrintingDetails>> listByOperator(@PathVariable String operatorNumber) {
        return ResponseEntity.ok(getByOperatorUseCase.execute(operatorNumber));
    }
    
    @GetMapping()
    public ResponseEntity<List<PrintingDetails>> listAll() {
        return ResponseEntity.ok(getPrintingOrdersUseCase.execute());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        
        OrderDataNotificationDTO oldOrderData = updateStatusUseCase.execute(id, status);

        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), status, oldOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        OrderDataNotificationDTO deletedOrderData = deleteUseCase.execute(id);

        queueEventPublisher.publishQueueUpdate(deletedOrderData.type(), deletedOrderData.status(), deletedOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }
}