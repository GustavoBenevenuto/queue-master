package com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;

public interface IStockWithdrawalDetailsJpaRepository extends JpaRepository<StockWithdrawalDetails, UUID> {
    
    Optional<StockWithdrawalDetails> findByOrderQueueId(UUID orderQueueId);
}