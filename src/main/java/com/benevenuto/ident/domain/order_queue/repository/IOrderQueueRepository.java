package com.benevenuto.ident.domain.order_queue.repository;

import java.util.List;
import java.util.UUID;

import com.benevenuto.ident.domain.order_queue.entity.OrderQueue;
import com.benevenuto.ident.enums.OrderStatus;
import com.benevenuto.ident.enums.RequestType;

public interface IOrderQueueRepository extends IBaseRepository<OrderQueue, UUID> {
    List<OrderQueue> findByStatusAndTypePrioritized(OrderStatus status, RequestType type);
    List<OrderQueue> findByTypePrioritized(RequestType type);
    List<OrderQueue> findByPwNumber(String pwNumber);
    long countByStatusAndType(OrderStatus status, RequestType type);    
}