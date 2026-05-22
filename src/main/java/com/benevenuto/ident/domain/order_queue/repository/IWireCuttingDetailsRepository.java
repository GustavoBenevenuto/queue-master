package com.benevenuto.ident.domain.order_queue.repository;

import java.util.Optional;
import java.util.UUID;

import com.benevenuto.ident.domain.order_queue.entity.WireCuttingDetails;

public interface IWireCuttingDetailsRepository extends IBaseRepository<WireCuttingDetails, UUID> {
    
    Optional<WireCuttingDetails> findByOrderQueueId(UUID orderQueueId);
}