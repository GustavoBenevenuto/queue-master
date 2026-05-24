package com.benevenuto.queue_master.application.printing_details;

import java.util.List;

import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetPrintingOrdersByOperatorUseCase {

    private final IPrintingDetailsRepository printingRepository;

    // CORREÇÃO: Alterado de List<PrintingDetails> para List<OrderResponseDTO>
    public List<PrintingDetails> execute(String operatorNumber) {
        return printingRepository.findByOperatorNumber(operatorNumber);
    }
}