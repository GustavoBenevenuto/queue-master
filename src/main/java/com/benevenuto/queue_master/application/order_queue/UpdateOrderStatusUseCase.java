package com.benevenuto.queue_master.application.order_queue;

import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.entity.OrderQueue;
import com.benevenuto.queue_master.domain.order_queue.repository.IOrderQueueRepository;
import com.benevenuto.queue_master.enums.OrderStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // O Lombok gera o construtor para o campo final IOrderQueueRepository
public class UpdateOrderStatusUseCase {
    
    private final IOrderQueueRepository repository;

    @Transactional
    public OrderDataNotificationDTO execute(UUID id, OrderStatus newStatus) {
        OrderQueue order = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        
        // Guardamos o status antigo antes de atualizar
        OrderStatus oldStatus = order.getStatus();
        
        order.setStatus(newStatus);
        repository.save(order);
        
        // Retorna o tipo e o status antigo para o controller saber quem notificar
        return new OrderDataNotificationDTO(order.getType(), oldStatus);
    }
}