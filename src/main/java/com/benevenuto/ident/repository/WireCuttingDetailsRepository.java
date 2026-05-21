package com.benevenuto.ident.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benevenuto.ident.entity.WireCuttingDetails;

@Repository
public interface WireCuttingDetailsRepository extends JpaRepository<WireCuttingDetails, UUID> {
    Optional<WireCuttingDetails> findByOrderQueueId(UUID orderQueueId);
}