package com.benevenuto.ident.infra.order_queue.repository.jpa.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.benevenuto.ident.domain.order_queue.entity.PrintingDetails;

public interface IPrintingDetailsJpaRepository extends JpaRepository<PrintingDetails, UUID> {
    Optional<PrintingDetails> findByOrderQueueId(UUID orderQueueId);
}