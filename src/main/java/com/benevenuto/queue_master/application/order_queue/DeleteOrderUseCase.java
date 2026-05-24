package com.benevenuto.queue_master.application.order_queue;

import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.entity.OrderQueue;
import com.benevenuto.queue_master.domain.order_queue.repository.IOrderQueueRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

public class DeleteOrderUseCase {

	private final IOrderQueueRepository repository;

	public DeleteOrderUseCase(IOrderQueueRepository repository) {
		this.repository = repository;
	}
	
	@Transactional
    public OrderDataNotificationDTO execute(UUID id) {
        OrderQueue order = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
            
        repository.deleteById(id);
        
        // Devolve os dados da ordem que acabou de ser destruída
        return new OrderDataNotificationDTO(order.getType(), order.getStatus(), order.getOperatorNumber());
    }
}
