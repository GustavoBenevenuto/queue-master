package com.benevenuto.ident.infra.order_queue.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.ident.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.ident.domain.order_queue.repository.IWireCuttingDetailsRepository;
import com.benevenuto.ident.infra.order_queue.repository.jpa.interfaces.IWireCuttingDetailsJpaRepository;

@Component
public class WireCuttingDetailsRepositoryJpaImpl 
       extends BaseRepositoryJpaImpl<WireCuttingDetails, UUID, IWireCuttingDetailsJpaRepository> 
       implements IWireCuttingDetailsRepository {

    public WireCuttingDetailsRepositoryJpaImpl(IWireCuttingDetailsJpaRepository repository) {
        super(repository);
    }

    @Override
    public Optional<WireCuttingDetails> findByOrderQueueId(UUID orderQueueId) {
        return repository.findByOrderQueueId(orderQueueId);
    }
}