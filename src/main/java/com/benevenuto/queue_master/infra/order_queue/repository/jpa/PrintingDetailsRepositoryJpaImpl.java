package com.benevenuto.queue_master.infra.order_queue.repository.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.infra.common.repository.jpa.interfaces.BaseRepositoryJpaImpl;
import com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces.IPrintingDetailsJpaRepository;

@Component
public class PrintingDetailsRepositoryJpaImpl 
       extends BaseRepositoryJpaImpl<PrintingDetails, UUID, IPrintingDetailsJpaRepository> 
       implements IPrintingDetailsRepository {

    public PrintingDetailsRepositoryJpaImpl(IPrintingDetailsJpaRepository repository) {
        super(repository);
    }

    @Override
    public List<PrintingDetails> findByStatusPrioritized(OrderStatus status) {
        return repository.findByStatusPrioritized(status);
    }

    @Override
    public List<PrintingDetails> findByOperatorNumber(String operatorNumber) {
        return repository.findByOperatorNumberPrioritized(operatorNumber);
    }

    @Override
    public List<PrintingDetails> findByPwNumber(String pwNumber) {
        return repository.findByPwNumber(pwNumber);
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return repository.countByStatus(status);
    }
}