package com.benevenuto.queue_master.application.wire_cutting_details;

import java.util.List;

import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetWireCuttingOrdersByOperatorUseCase {

    private final IWireCuttingDetailsRepository wireRepository;

    public List<WireCuttingDetails> execute(String operatorNumber) {
        return wireRepository.findByOperatorNumber(operatorNumber);
    }
}