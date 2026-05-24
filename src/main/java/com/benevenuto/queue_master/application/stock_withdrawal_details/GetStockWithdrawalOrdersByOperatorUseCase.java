package com.benevenuto.queue_master.application.stock_withdrawal_details;

import java.util.List;

import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IStockWithdrawalDetailsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetStockWithdrawalOrdersByOperatorUseCase {

    private final IStockWithdrawalDetailsRepository stockRepository;

    public List<StockWithdrawalDetails> execute(String operatorNumber) {
        return stockRepository.findByOperatorNumber(operatorNumber);
    }
}