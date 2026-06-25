package com.benevenuto.queue_master.presentation.printing.controller;

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

import com.benevenuto.queue_master.presentation.printing.dto.PrintingOrderRequestDTO;
import com.benevenuto.queue_master.application.printing_details.CreatePrintingOrderUseCase;
import com.benevenuto.queue_master.application.printing_details.DeletePrintingOrderUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersUseCase;
import com.benevenuto.queue_master.application.printing_details.UpdatePrintingOrderStatusUseCase;
import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;
import com.benevenuto.queue_master.domain.common.enums.QueueEventType;
import com.benevenuto.queue_master.presentation.printing.websocket.PrintingQueueEventPublisher;

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
    private final PrintingQueueEventPublisher queueEventPublisher;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody List<PrintingOrderRequestDTO> dto) {
        List<PrintingDetails> createdOrders = createUseCase.execute(dto);

        // Cada ordem criada gera seu próprio evento de delta no WebSocket, com o objeto completo
        createdOrders.forEach(order ->
            queueEventPublisher.publishQueueUpdate(QueueEventType.ORDER_CREATED, order)
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

        PrintingDetails updatedOrder = updateStatusUseCase.execute(id, status);

        queueEventPublisher.publishQueueUpdate(QueueEventType.STATUS_CHANGED, updatedOrder);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        PrintingDetails deletedOrder = deleteUseCase.execute(id);

        queueEventPublisher.publishQueueUpdate(QueueEventType.ORDER_DELETED, deletedOrder);

        return ResponseEntity.noContent().build();
    }
}
