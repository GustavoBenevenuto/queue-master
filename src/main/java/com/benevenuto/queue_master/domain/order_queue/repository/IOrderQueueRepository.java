package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.List;
import java.util.UUID;

import com.benevenuto.queue_master.domain.order_queue.entity.OrderQueue;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

public interface IOrderQueueRepository extends IBaseRepository<OrderQueue, UUID> {
    List<OrderQueue> findByStatusAndTypePrioritized(OrderStatus status, RequestType type);
    List<OrderQueue> findByTypePrioritized(RequestType type);
    List<OrderQueue> findByPwNumber(String pwNumber);
    long countByStatusAndType(OrderStatus status, RequestType type);    
}