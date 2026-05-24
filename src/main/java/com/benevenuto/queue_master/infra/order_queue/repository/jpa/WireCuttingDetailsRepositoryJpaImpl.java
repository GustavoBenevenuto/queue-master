package com.benevenuto.queue_master.infra.order_queue.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.infra.common.repository.jpa.interfaces.BaseRepositoryJpaImpl;
import com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces.IWireCuttingDetailsJpaRepository;

@Component
public class WireCuttingDetailsRepositoryJpaImpl 
       extends BaseRepositoryJpaImpl<WireCuttingDetails, UUID, IWireCuttingDetailsJpaRepository> 
       implements IWireCuttingDetailsRepository {

    public WireCuttingDetailsRepositoryJpaImpl(IWireCuttingDetailsJpaRepository repository) {
        super(repository);
    }

    @Override
    public List<WireCuttingDetails> findByStatusPrioritized(OrderStatus status) {
        return repository.findByStatusPrioritized(status);
    }

    @Override
    public List<WireCuttingDetails> findByOperatorNumber(String operatorNumber) {
        return repository.findByOperatorNumberPrioritized(operatorNumber);
    }

    @Override
    public List<WireCuttingDetails> findByPwNumber(String pwNumber) {
        return repository.findByPwNumber(pwNumber);
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return repository.countByStatus(status);
    }
}