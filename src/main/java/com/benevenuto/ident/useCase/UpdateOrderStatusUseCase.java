package com.benevenuto.ident.useCase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.benevenuto.ident.entity.OrderQueue;
import com.benevenuto.ident.enums.OrderStatus;
import com.benevenuto.ident.repository.OrderQueueRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {
    private final OrderQueueRepository repository;

    @Transactional
    public void execute(UUID id, OrderStatus newStatus) {
        OrderQueue order = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(newStatus);
        repository.save(order);
    }
}