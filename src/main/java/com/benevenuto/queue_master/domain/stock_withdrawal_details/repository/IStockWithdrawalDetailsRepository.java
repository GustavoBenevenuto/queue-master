package com.benevenuto.queue_master.domain.stock_withdrawal_details.repository;

import java.util.UUID;

import com.benevenuto.queue_master.domain.common.repository.IBaseRepositoryOrder;
import com.benevenuto.queue_master.domain.stock_withdrawal_details.entity.StockWithdrawalDetails;

public interface IStockWithdrawalDetailsRepository extends IBaseRepositoryOrder<StockWithdrawalDetails, UUID> {}