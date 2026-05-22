package com.benevenuto.ident.application.order_queue;

import java.util.UUID;

import com.benevenuto.ident.domain.order_queue.repository.IOrderQueueRepository;

import jakarta.transaction.Transactional;

public class DeleteOrderUseCase {

	private final IOrderQueueRepository repository;

	public DeleteOrderUseCase(IOrderQueueRepository repository) {
		this.repository = repository;
	}
	
    @Transactional
    public void execute(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        repository.deleteById(id);
    }
}
