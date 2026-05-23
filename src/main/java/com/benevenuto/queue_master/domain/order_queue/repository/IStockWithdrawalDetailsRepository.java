package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;

public interface IStockWithdrawalDetailsRepository extends IBaseRepository<StockWithdrawalDetails, UUID> {
    
    Optional<StockWithdrawalDetails> findByOrderQueueId(UUID orderQueueId);
}