package com.benevenuto.queue_master.infra.config.stock_withdrawal_details;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.benevenuto.queue_master.application.stock_withdrawal_details.CreateStockWithdrawalOrderUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.DeleteStockWithdrawalOrderUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.UpdateStockWithdrawalOrderStatusUseCase;
import com.benevenuto.queue_master.domain.order_queue.repository.IStockWithdrawalDetailsRepository;

@Configuration
public class StockWithdrawalDetailsConfig {

    @Bean
    public CreateStockWithdrawalOrderUseCase createStockWithdrawalOrderUseCase(
        IStockWithdrawalDetailsRepository stockRepo
    ) {
        return new CreateStockWithdrawalOrderUseCase(stockRepo);
    }
    
    @Bean
    public DeleteStockWithdrawalOrderUseCase deleteStockWithdrawalOrderUseCase(
        IStockWithdrawalDetailsRepository stockRepo
    ) {
        return new DeleteStockWithdrawalOrderUseCase(stockRepo);
    }

    @Bean
    public GetStockWithdrawalOrdersByOperatorUseCase getStockWithdrawalOrdersByOperatorUseCase(
        IStockWithdrawalDetailsRepository stockRepo
    ) {
        return new GetStockWithdrawalOrdersByOperatorUseCase(stockRepo);
    }

    @Bean
    public UpdateStockWithdrawalOrderStatusUseCase updateStockWithdrawalOrderStatusUseCase(
        IStockWithdrawalDetailsRepository stockRepo
    ) {
        return new UpdateStockWithdrawalOrderStatusUseCase(stockRepo);
    }
}