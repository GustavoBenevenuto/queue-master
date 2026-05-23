package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;

public interface IWireCuttingDetailsRepository extends IBaseRepository<WireCuttingDetails, UUID> {
    
    Optional<WireCuttingDetails> findByOrderQueueId(UUID orderQueueId);
}