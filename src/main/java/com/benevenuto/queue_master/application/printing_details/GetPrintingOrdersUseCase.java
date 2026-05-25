package com.benevenuto.queue_master.application.printing_details;

import java.util.List;

import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetPrintingOrdersUseCase {

    private final IPrintingDetailsRepository printingRepository;

    public List<PrintingDetails> execute() {
        return printingRepository.findAll();
    }
}