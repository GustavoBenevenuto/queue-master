package com.benevenuto.queue_master.infra.wire_cutting_details.repository.jpa.interfaces;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.benevenuto.queue_master.domain.wire_cutting_details.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

public interface IWireCuttingDetailsJpaRepository extends JpaRepository<WireCuttingDetails, UUID> {

    @Query("SELECT w FROM WireCuttingDetails w WHERE w.status = :status ORDER BY w.isUrgent DESC, w.createdAt ASC")
    List<WireCuttingDetails> findByStatusPrioritized(@Param("status") OrderStatus status);

    @Query("SELECT w FROM WireCuttingDetails w WHERE w.operatorNumber = :operatorNumber ORDER BY w.isUrgent DESC, w.createdAt DESC")
    List<WireCuttingDetails> findByOperatorNumberPrioritized(@Param("operatorNumber") String operatorNumber);

    List<WireCuttingDetails> findByWorkOrderNumber(String workOrderNumber);

    long countByStatus(OrderStatus status);
}