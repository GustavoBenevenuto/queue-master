package com.benevenuto.queue_master.infra.config.wire_cutting_details;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.benevenuto.queue_master.application.wire_cutting_details.CreateWireCuttingOrderUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.DeleteWireCuttingOrderUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.UpdateWireCuttingOrderStatusUseCase;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;

@Configuration
public class WireCuttingDetailsConfig {

    @Bean
    public CreateWireCuttingOrderUseCase createWireCuttingOrderUseCase(
        IWireCuttingDetailsRepository wireRepo
    ) {
        return new CreateWireCuttingOrderUseCase(wireRepo);
    }
    
    @Bean
    public DeleteWireCuttingOrderUseCase deleteWireCuttingOrderUseCase(
        IWireCuttingDetailsRepository wireRepo
    ) {
        return new DeleteWireCuttingOrderUseCase(wireRepo);
    }

    @Bean
    public GetWireCuttingOrdersByOperatorUseCase getWireCuttingOrdersByOperatorUseCase(
        IWireCuttingDetailsRepository wireRepo
    ) {
        return new GetWireCuttingOrdersByOperatorUseCase(wireRepo);
    }

    @Bean
    public UpdateWireCuttingOrderStatusUseCase updateWireCuttingOrderStatusUseCase(
        IWireCuttingDetailsRepository wireRepo
    ) {
        return new UpdateWireCuttingOrderStatusUseCase(wireRepo);
    }
}