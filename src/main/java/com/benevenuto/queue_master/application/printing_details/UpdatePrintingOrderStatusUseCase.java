package com.benevenuto.queue_master.application.printing_details;

import java.util.UUID;

import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.printing_details.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdatePrintingOrderStatusUseCase {

    private final IPrintingDetailsRepository printingRepository;

    @Transactional
    public PrintingDetails execute(UUID id, OrderStatus newStatus) {
        return printingRepository.findById(id)
            .map(entity -> {
                entity.setStatus(newStatus);
                return printingRepository.save(entity);
            })
            .orElseThrow(() -> new EntityNotFoundException("Printing order with ID " + id + " not found."));
    }
}
