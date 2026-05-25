package com.benevenuto.queue_master.presentation.order_queue.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.application.stock_withdrawal_details.CreateStockWithdrawalOrderUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.DeleteStockWithdrawalOrderUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.UpdateStockWithdrawalOrderStatusUseCase;
import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.presentation.websocket.QueueEventPublisher;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders/stock-withdrawal")
@RequiredArgsConstructor
public class StockWithdrawalDetailsController {

    private final CreateStockWithdrawalOrderUseCase createUseCase;
    private final DeleteStockWithdrawalOrderUseCase deleteUseCase;
    private final GetStockWithdrawalOrdersByOperatorUseCase getByOperatorUseCase;
    private final GetStockWithdrawalOrdersUseCase getStockWithdrawalOrdersUseCase;
    private final UpdateStockWithdrawalOrderStatusUseCase updateStatusUseCase;
    private final QueueEventPublisher queueEventPublisher;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody List<StockWithdrawalDetails> dto, @RequestParam String opNumber) {
        List<OrderDataNotificationDTO> createdOrders = createUseCase.execute(dto);

        // AJUSTADO: Dispara o evento combinado (Geral + Operador) para cada tipo criado
        createdOrders.stream()
            .distinct()
            .forEach(notification -> 
                queueEventPublisher.publishQueueUpdate(notification.type(), notification.status(), opNumber)
            );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/operator/{operatorNumber}")
    public ResponseEntity<List<StockWithdrawalDetails>> listByOperator(@PathVariable String operatorNumber) {
        return ResponseEntity.ok(getByOperatorUseCase.execute(operatorNumber));
    }
    
    @GetMapping()
    public ResponseEntity<List<StockWithdrawalDetails>> listAll() {
        return ResponseEntity.ok(getStockWithdrawalOrdersUseCase.execute());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        
        OrderDataNotificationDTO oldOrderData = updateStatusUseCase.execute(id, status);

        // AJUSTADO: Passa o status novo e o operatorNumber no mesmo evento unificado
        queueEventPublisher.publishQueueUpdate(oldOrderData.type(), status, oldOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        OrderDataNotificationDTO deletedOrderData = deleteUseCase.execute(id);

        // AJUSTADO: Atualiza ambos os canais após a deleção
        queueEventPublisher.publishQueueUpdate(deletedOrderData.type(), deletedOrderData.status(), deletedOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }
}