package com.benevenuto.queue_master.infra.printing_details.repository.jpa.interfaces;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

public interface IPrintingDetailsJpaRepository extends JpaRepository<PrintingDetails, UUID> {

    @Query("SELECT p FROM PrintingDetails p WHERE p.status = :status ORDER BY p.isUrgent DESC, p.createdAt ASC")
    List<PrintingDetails> findByStatusPrioritized(@Param("status") OrderStatus status);

    @Query("SELECT p FROM PrintingDetails p WHERE p.operatorNumber = :operatorNumber ORDER BY p.isUrgent DESC, p.createdAt DESC")
    List<PrintingDetails> findByOperatorNumberPrioritized(@Param("operatorNumber") String operatorNumber);

    List<PrintingDetails> findByWorkOrderNumber(String workOrderNumber);

    long countByStatus(OrderStatus status);
}