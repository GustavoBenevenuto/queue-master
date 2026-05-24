package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.List;
import java.util.UUID;
import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.enums.OrderStatus;

public interface IPrintingDetailsRepository extends IBaseRepository<PrintingDetails, UUID> {
    List<PrintingDetails> findByStatusPrioritized(OrderStatus status);
    List<PrintingDetails> findByOperatorNumber(String operatorNumber);
    List<PrintingDetails> findByPwNumber(String pwNumber);
    long countByStatus(OrderStatus status);
}