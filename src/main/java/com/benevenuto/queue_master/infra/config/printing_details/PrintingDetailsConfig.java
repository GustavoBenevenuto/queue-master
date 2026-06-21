package com.benevenuto.queue_master.infra.config.printing_details;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.benevenuto.queue_master.application.printing_details.CreatePrintingOrderUseCase;
import com.benevenuto.queue_master.application.printing_details.DeletePrintingOrderUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersUseCase;
import com.benevenuto.queue_master.application.printing_details.UpdatePrintingOrderStatusUseCase;
import com.benevenuto.queue_master.domain.printing_details.repository.IPrintingDetailsRepository;

@Configuration
public class PrintingDetailsConfig {

    @Bean
    public CreatePrintingOrderUseCase createOrderUseCase(
        IPrintingDetailsRepository printRepo
    ) {
        return new CreatePrintingOrderUseCase(printRepo);
    }
    
    @Bean
    public DeletePrintingOrderUseCase deleteOrderUseCase(
        IPrintingDetailsRepository printRepo
    ) {
        return new DeletePrintingOrderUseCase(printRepo);
    }

    @Bean
    public GetPrintingOrdersByOperatorUseCase getOrdersByOperatorUseCase(
        IPrintingDetailsRepository printRepo
    ) {
        return new GetPrintingOrdersByOperatorUseCase(printRepo);
    }

    @Bean
    public GetPrintingOrdersUseCase getPrintingOrdersUseCase(
        IPrintingDetailsRepository printRepo
    ) {
        return new GetPrintingOrdersUseCase(printRepo);
    }

    @Bean
    public UpdatePrintingOrderStatusUseCase updateOrderStatusUseCase(
        IPrintingDetailsRepository printRepo
    ) {
        return new UpdatePrintingOrderStatusUseCase(printRepo);
    }
}