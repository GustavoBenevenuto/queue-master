package com.benevenuto.ident.useCase;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benevenuto.ident.repository.OrderQueueRepository;

import jakarta.transaction.Transactional;

@Service
public class DeleteOrderUseCase {

    @Autowired
    private OrderQueueRepository repository;

    @Transactional
    public void execute(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        repository.deleteById(id);
    }
}