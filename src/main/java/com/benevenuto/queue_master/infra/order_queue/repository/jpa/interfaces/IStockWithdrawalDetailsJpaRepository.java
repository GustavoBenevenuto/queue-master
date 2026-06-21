package com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.domain.order_queue.enums.OrderStatus;

public interface IStockWithdrawalDetailsJpaRepository extends JpaRepository<StockWithdrawalDetails, UUID> {

    @Query("SELECT s FROM StockWithdrawalDetails s WHERE s.status = :status ORDER BY s.isUrgent DESC, s.createdAt ASC")
    List<StockWithdrawalDetails> findByStatusPrioritized(@Param("status") OrderStatus status);

    @Query("SELECT s FROM StockWithdrawalDetails s WHERE s.operatorNumber = :operatorNumber ORDER BY s.isUrgent DESC, s.createdAt DESC")
    List<StockWithdrawalDetails> findByOperatorNumberPrioritized(@Param("operatorNumber") String operatorNumber);

    List<StockWithdrawalDetails> findByWorkOrderNumber(String workOrderNumber);

    long countByStatus(OrderStatus status);
}