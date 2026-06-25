package com.benevenuto.queue_master.application.wire_cutting_details;

import java.util.UUID;

import com.benevenuto.queue_master.domain.wire_cutting_details.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.wire_cutting_details.repository.IWireCuttingDetailsRepository;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateWireCuttingOrderStatusUseCase {

    private final IWireCuttingDetailsRepository wireRepository;

    @Transactional
    public WireCuttingDetails execute(UUID id, OrderStatus newStatus) {
        return wireRepository.findById(id)
            .map(entity -> {
                entity.setStatus(newStatus);
                return wireRepository.save(entity);
            })
            .orElseThrow(() -> new EntityNotFoundException("Wire cutting order with ID " + id + " not found."));
    }
}
