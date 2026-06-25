package com.benevenuto.queue_master.application.printing_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.presentation.printing.dto.PrintingOrderRequestDTO;
import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.printing_details.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreatePrintingOrderUseCase {

    private final IPrintingDetailsRepository printingRepository;

    @Transactional
    public List<PrintingDetails> execute(List<PrintingOrderRequestDTO> dto) {
        List<PrintingDetails> createdOrders = new ArrayList<>();

        Optional.ofNullable(dto).ifPresent(list ->
            list.forEach(item -> {
                PrintingDetails order = PrintingDetails.builder()
                    .id(UUID.randomUUID())
                    .workOrderNumber(item.getWorkOrderNumber())
                    .operatorNumber(item.getOperatorNumber())
                    .printText(item.getPrintText())
                    .quantity(item.getQuantity())
                    .isUrgent(item.getIsUrgent())
                    .reason(item.getReason())
                    .status(OrderStatus.pending)
                    .build();

                printingRepository.save(order);
                createdOrders.add(order);
            }));

        return createdOrders;
    }
}
