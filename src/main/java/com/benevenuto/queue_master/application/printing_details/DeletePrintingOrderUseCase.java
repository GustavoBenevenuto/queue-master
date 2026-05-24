package com.benevenuto.queue_master.application.printing_details;

import java.util.UUID;
import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.enums.RequestType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeletePrintingOrderUseCase {

    private final IPrintingDetailsRepository printingRepository;
    
    @Transactional
    public OrderDataNotificationDTO execute(UUID id) {
        return printingRepository.findById(id)
            .map(entity -> {
                printingRepository.deleteById(id);
                return new OrderDataNotificationDTO(
                    RequestType.identification_printing, 
                    entity.getStatus(), 
                    entity.getOperatorNumber()
                );
            })
            .orElseThrow(() -> new EntityNotFoundException("Printing order with ID " + id + " not found."));
    }
}