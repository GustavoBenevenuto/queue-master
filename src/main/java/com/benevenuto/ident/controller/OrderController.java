package com.benevenuto.ident.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.benevenuto.ident.DTO.OrderRequestDTO;
import com.benevenuto.ident.DTO.OrderResponseDTO;
import com.benevenuto.ident.enums.OrderStatus;
import com.benevenuto.ident.enums.RequestType;
import com.benevenuto.ident.useCase.CreateOrderUseCase;
import com.benevenuto.ident.useCase.DeleteOrderUseCase;
import com.benevenuto.ident.useCase.GetQueueByStationUseCase;
import com.benevenuto.ident.useCase.UpdateOrderStatusUseCase;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private GetQueueByStationUseCase getQueueByStationUseCase;

    @Autowired
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Autowired
    private DeleteOrderUseCase deleteOrderUseCase;

    // Criar novos pedidos (Suporta múltiplos tipos no mesmo JSON)
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody OrderRequestDTO dto) {
        createOrderUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Listar fila de uma estação específica (Ex: /queue/wire_cutting?status=pending)
    @GetMapping("/queue/{type}")
    public ResponseEntity<List<OrderResponseDTO>> listByStation(
            @PathVariable RequestType type,
            @RequestParam(defaultValue = "pending") OrderStatus status) {
        return ResponseEntity.ok(getQueueByStationUseCase.execute(type, status));
    }

    // Atualizar apenas o status (Ex: Mudar para 'in_process' ou 'finished')
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        updateOrderStatusUseCase.execute(id, status);
        return ResponseEntity.noContent().build();
    }

    // Excluir um pedido da fila
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteOrderUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}