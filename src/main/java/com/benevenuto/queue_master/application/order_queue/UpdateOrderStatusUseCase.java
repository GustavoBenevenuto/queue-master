package com.benevenuto.queue_master.application.order_queue;

import java.util.UUID;
import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.entity.OrderQueue;
import com.benevenuto.queue_master.domain.order_queue.repository.IOrderQueueRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {
    
    private final IOrderQueueRepository repository;

    @Transactional
    public OrderDataNotificationDTO execute(UUID id, OrderStatus newStatus) {
        OrderQueue order = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        
        OrderStatus oldStatus = order.getStatus();
        
        order.setStatus(newStatus);
        repository.save(order);
        
        // Agora retornamos o tipo, o status antigo E o número do operador dono da ordem
        return new OrderDataNotificationDTO(order.getType(), oldStatus, order.getOperatorNumber());
    }
}