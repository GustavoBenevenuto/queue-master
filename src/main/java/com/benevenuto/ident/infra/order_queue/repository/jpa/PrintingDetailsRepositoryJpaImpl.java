package com.benevenuto.ident.infra.order_queue.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.ident.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.ident.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.ident.infra.order_queue.repository.jpa.interfaces.IPrintingDetailsJpaRepository;

@Component
public class PrintingDetailsRepositoryJpaImpl 
       extends BaseRepositoryJpaImpl<PrintingDetails, UUID, IPrintingDetailsJpaRepository> 
       implements IPrintingDetailsRepository {

    public PrintingDetailsRepositoryJpaImpl(IPrintingDetailsJpaRepository repository) {
        super(repository);
    }

    @Override
    public Optional<PrintingDetails> findByOrderQueueId(UUID orderQueueId) {
        return repository.findByOrderQueueId(orderQueueId);
    }
}