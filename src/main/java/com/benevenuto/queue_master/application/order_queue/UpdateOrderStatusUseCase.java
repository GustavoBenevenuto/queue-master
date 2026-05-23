package com.benevenuto.queue_master.application.order_queue;

import java.util.UUID;

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
    public void execute(UUID id, OrderStatus newStatus) {
        // Agora utilizamos a interface de domínio IOrderQueueRepository
        OrderQueue order = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        
        order.setStatus(newStatus);
        
        // O método save aqui segue o contrato da interface de domínio
        repository.save(order);
    }
}