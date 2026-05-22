package com.benevenuto.ident.domain.order_queue.repository;

import java.util.Optional;
import java.util.UUID;

import com.benevenuto.ident.domain.order_queue.entity.PrintingDetails;

public interface IPrintingDetailsRepository extends IBaseRepository<PrintingDetails, UUID> {
    Optional<PrintingDetails> findByOrderQueueId(UUID orderQueueId);
}