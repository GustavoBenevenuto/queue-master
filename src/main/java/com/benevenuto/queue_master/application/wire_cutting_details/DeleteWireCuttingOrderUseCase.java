package com.benevenuto.queue_master.application.wire_cutting_details;

import java.util.UUID;

import com.benevenuto.queue_master.domain.wire_cutting_details.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.wire_cutting_details.repository.IWireCuttingDetailsRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteWireCuttingOrderUseCase {

    private final IWireCuttingDetailsRepository wireRepository;

    @Transactional
    public WireCuttingDetails execute(UUID id) {
        return wireRepository.findById(id)
            .map(entity -> {
                wireRepository.deleteById(id);
                return entity;
            })
            .orElseThrow(() -> new EntityNotFoundException("Wire cutting order with ID " + id + " not found."));
    }
}
