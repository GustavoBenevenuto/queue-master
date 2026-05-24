package com.benevenuto.queue_master.infra.config.order_queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.benevenuto.queue_master.application.order_queue.CreateOrderUseCase;
import com.benevenuto.queue_master.application.order_queue.DeleteOrderUseCase;
import com.benevenuto.queue_master.application.order_queue.GetOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.order_queue.GetQueueByStationUseCase;
import com.benevenuto.queue_master.application.order_queue.UpdateOrderStatusUseCase;
import com.benevenuto.queue_master.domain.order_queue.repository.IOrderQueueRepository;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.domain.order_queue.repository.IStockWithdrawalDetailsRepository;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;

@Configuration
public class OrderQueueConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(
        IOrderQueueRepository queueRepo,
        IPrintingDetailsRepository printRepo,
        IWireCuttingDetailsRepository wireRepo,
        IStockWithdrawalDetailsRepository stockRepo
    ) {
        return new CreateOrderUseCase(queueRepo, printRepo, wireRepo, stockRepo);
    }
    
    @Bean
    public DeleteOrderUseCase deleteOrderUseCase(IOrderQueueRepository queueRepo) {
        return new DeleteOrderUseCase(queueRepo);
    }

    @Bean
    public GetQueueByStationUseCase getQueueByStationUseCase(
        IOrderQueueRepository queueRepo,
        IPrintingDetailsRepository printRepo,
        IWireCuttingDetailsRepository wireRepo,
        IStockWithdrawalDetailsRepository stockRepo
    ) {
        return new GetQueueByStationUseCase(queueRepo, printRepo, wireRepo, stockRepo);
    }

    @Bean
    public GetOrdersByOperatorUseCase getOrdersByOperatorUseCase(
        IOrderQueueRepository queueRepo,
        IPrintingDetailsRepository printRepo,
        IWireCuttingDetailsRepository wireRepo,
        IStockWithdrawalDetailsRepository stockRepo
    ) {
        return new GetOrdersByOperatorUseCase(queueRepo, printRepo, wireRepo, stockRepo);
    }

    @Bean
    public UpdateOrderStatusUseCase updateOrderStatusUseCase(IOrderQueueRepository queueRepo) {
        return new UpdateOrderStatusUseCase(queueRepo);
    }
}
