package com.benevenuto.queue_master.infra.order_queue.repository.jpa;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.infra.common.repository.jpa.interfaces.BaseRepositoryOrderJpaImpl;
import com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces.IPrintingDetailsJpaRepository;

@Component
public class PrintingDetailsRepositoryJpaImpl 
       extends BaseRepositoryOrderJpaImpl<PrintingDetails, UUID, IPrintingDetailsJpaRepository> 
       implements IPrintingDetailsRepository {

    public PrintingDetailsRepositoryJpaImpl(IPrintingDetailsJpaRepository repository) {
        super(repository, PrintingDetails.class);
    }
}