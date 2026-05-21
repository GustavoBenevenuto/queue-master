package com.benevenuto.ident.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benevenuto.ident.entity.StockWithdrawalDetails;

@Repository
public interface StockWithdrawalDetailsRepository extends JpaRepository<StockWithdrawalDetails, UUID> {
    Optional<StockWithdrawalDetails> findByOrderQueueId(UUID orderQueueId);
}