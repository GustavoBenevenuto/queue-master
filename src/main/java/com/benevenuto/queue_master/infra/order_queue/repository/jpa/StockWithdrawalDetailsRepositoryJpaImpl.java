package com.benevenuto.queue_master.infra.order_queue.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IStockWithdrawalDetailsRepository;
import com.benevenuto.queue_master.infra.common.repository.jpa.interfaces.BaseRepositoryJpaImpl;
import com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces.IStockWithdrawalDetailsJpaRepository;

@Component
public class StockWithdrawalDetailsRepositoryJpaImpl 
       extends BaseRepositoryJpaImpl<StockWithdrawalDetails, UUID, IStockWithdrawalDetailsJpaRepository> 
       implements IStockWithdrawalDetailsRepository {

    public StockWithdrawalDetailsRepositoryJpaImpl(IStockWithdrawalDetailsJpaRepository repository) {
        super(repository);
    }

    @Override
    public Optional<StockWithdrawalDetails> findByOrderQueueId(UUID orderQueueId) {
        return repository.findByOrderQueueId(orderQueueId);
    }
}