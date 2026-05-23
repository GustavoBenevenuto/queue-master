package com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.benevenuto.queue_master.domain.order_queue.entity.OrderQueue;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

public interface IOrderQueueJpaRepository extends JpaRepository<OrderQueue, UUID> {

    @Query("SELECT o FROM OrderQueue o " +
           "LEFT JOIN PrintingDetails p ON o.id = p.orderQueue.id " +
           "LEFT JOIN WireCuttingDetails w ON o.id = w.orderQueue.id " +
           "LEFT JOIN StockWithdrawalDetails s ON o.id = s.orderQueue.id " +
           "WHERE o.status = :status AND o.type = :type " +
           "ORDER BY CASE " +
           "  WHEN o.type = 'identification_printing' THEN p.isUrgent " +
           "  WHEN o.type = 'wire_cutting' THEN w.isUrgent " +
           "  WHEN o.type = 'stock_withdrawal' THEN s.isUrgent " +
           "  ELSE false END DESC, o.createdAt ASC")
    List<OrderQueue> findByStatusAndTypePrioritized(@Param("status") OrderStatus status, @Param("type") RequestType type);

    @Query("SELECT o FROM OrderQueue o " +
           "LEFT JOIN PrintingDetails p ON o.id = p.orderQueue.id " +
           "LEFT JOIN WireCuttingDetails w ON o.id = w.orderQueue.id " +
           "LEFT JOIN StockWithdrawalDetails s ON o.id = s.orderQueue.id " +
           "WHERE o.type = :type " +
           "ORDER BY CASE " +
           "  WHEN o.type = 'identification_printing' THEN p.isUrgent " +
           "  WHEN o.type = 'wire_cutting' THEN w.isUrgent " +
           "  WHEN o.type = 'stock_withdrawal' THEN s.isUrgent " +
           "  ELSE false END DESC, o.createdAt ASC")
    List<OrderQueue> findByTypePrioritized(@Param("type") RequestType type);

    List<OrderQueue> findByPwNumber(String pwNumber);

    long countByStatusAndType(OrderStatus status, RequestType type);
}