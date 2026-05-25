package com.benevenuto.queue_master.presentation.stock_withdrawal.controller;

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
import com.benevenuto.queue_master.application.stock_withdrawal_details.CreateStockWithdrawalOrderUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.DeleteStockWithdrawalOrderUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.UpdateStockWithdrawalOrderStatusUseCase;
import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.presentation.stock_withdrawal.websocket.StockWithdrawalQueueEventPublisher;

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
    private final StockWithdrawalQueueEventPublisher queueEventPublisher; // AJUSTADO: Publisher de Estoque

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody List<StockWithdrawalDetails> dto, @RequestParam String opNumber) {
        List<OrderDataNotificationDTO> createdOrders = createUseCase.execute(dto);

        // AJUSTADO: Chamada direta sem RequestType
        createdOrders.stream()
            .distinct()
            .forEach(notification -> 
                queueEventPublisher.publishQueueUpdate(notification.status(), opNumber)
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

        // AJUSTADO: Chamada simplificada para o websocket específico
        queueEventPublisher.publishQueueUpdate(status, oldOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        OrderDataNotificationDTO deletedOrderData = deleteUseCase.execute(id);

        // AJUSTADO: Chamada simplificada para o websocket específico
        queueEventPublisher.publishQueueUpdate(deletedOrderData.status(), deletedOrderData.operatorNumber());

        return ResponseEntity.noContent().build();
    }
}