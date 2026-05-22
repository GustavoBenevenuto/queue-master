package com.benevenuto.ident.domain.order_queue.repository;

import java.util.Optional;
import java.util.UUID;

import com.benevenuto.ident.domain.order_queue.entity.StockWithdrawalDetails;

public interface IStockWithdrawalDetailsRepository extends IBaseRepository<StockWithdrawalDetails, UUID> {
    
    Optional<StockWithdrawalDetails> findByOrderQueueId(UUID orderQueueId);
}