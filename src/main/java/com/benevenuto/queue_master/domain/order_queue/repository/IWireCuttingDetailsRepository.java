package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.List;
import java.util.UUID;
import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.queue_master.enums.OrderStatus;

public interface IWireCuttingDetailsRepository extends IBaseRepository<WireCuttingDetails, UUID> {
    List<WireCuttingDetails> findByStatusPrioritized(OrderStatus status);
    List<WireCuttingDetails> findByOperatorNumber(String operatorNumber);
    List<WireCuttingDetails> findByPwNumber(String pwNumber);
    long countByStatus(OrderStatus status);
}