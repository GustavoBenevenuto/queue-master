package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.List;
import java.util.UUID;
import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.enums.OrderStatus;

public interface IStockWithdrawalDetailsRepository extends IBaseRepository<StockWithdrawalDetails, UUID> {
    List<StockWithdrawalDetails> findByStatusPrioritized(OrderStatus status);
    List<StockWithdrawalDetails> findByOperatorNumber(String operatorNumber);
    List<StockWithdrawalDetails> findByPwNumber(String pwNumber);
    long countByStatus(OrderStatus status);
}