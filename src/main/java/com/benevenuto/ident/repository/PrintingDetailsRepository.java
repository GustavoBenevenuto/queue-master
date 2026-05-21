package com.benevenuto.ident.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benevenuto.ident.entity.PrintingDetails;

@Repository
public interface PrintingDetailsRepository extends JpaRepository<PrintingDetails, UUID> {
    // Busca os detalhes técnicos através do ID da fila principal
    Optional<PrintingDetails> findByOrderQueueId(UUID orderQueueId);
}