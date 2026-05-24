package com.benevenuto.queue_master.infra.order_queue.repository.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IStockWithdrawalDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
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
    public List<StockWithdrawalDetails> findByStatusPrioritized(OrderStatus status) {
        return repository.findByStatusPrioritized(status);
    }

    @Override
    public List<StockWithdrawalDetails> findByOperatorNumber(String operatorNumber) {
        return repository.findByOperatorNumberPrioritized(operatorNumber);
    }

    @Override
    public List<StockWithdrawalDetails> findByPwNumber(String pwNumber) {
        return repository.findByPwNumber(pwNumber);
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return repository.countByStatus(status);
    }
}