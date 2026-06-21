package com.benevenuto.queue_master.application.stock_withdrawal_details;

import java.util.List;

import com.benevenuto.queue_master.domain.stock_withdrawal_details.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.domain.stock_withdrawal_details.repository.IStockWithdrawalDetailsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetStockWithdrawalOrdersUseCase {

    private final IStockWithdrawalDetailsRepository stockRepository;

    public List<StockWithdrawalDetails> execute() {
        return stockRepository.findAll();
    }
}