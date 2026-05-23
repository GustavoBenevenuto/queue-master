package com.benevenuto.queue_master.application.order_queue;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.benevenuto.queue_master.DTO.OrderResponseDTO;
import com.benevenuto.queue_master.domain.order_queue.entity.OrderQueue;
import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.domain.order_queue.entity.StockWithdrawalDetails;
import com.benevenuto.queue_master.domain.order_queue.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.order_queue.repository.IOrderQueueRepository;
import com.benevenuto.queue_master.domain.order_queue.repository.IPrintingDetailsRepository;
import com.benevenuto.queue_master.domain.order_queue.repository.IStockWithdrawalDetailsRepository;
import com.benevenuto.queue_master.domain.order_queue.repository.IWireCuttingDetailsRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

public class GetQueueByStationUseCase {

    private final IOrderQueueRepository queueRepository;
    private final IPrintingDetailsRepository printingRepository;
    private final IWireCuttingDetailsRepository wireRepository;
    private final IStockWithdrawalDetailsRepository stockRepository;

    // Construtor único para injeção de dependência (Clean IoC)
    public GetQueueByStationUseCase(
        IOrderQueueRepository queueRepository,
        IPrintingDetailsRepository printingRepository,
        IWireCuttingDetailsRepository wireRepository,
        IStockWithdrawalDetailsRepository stockRepository
    ) {
        this.queueRepository = queueRepository;
        this.printingRepository = printingRepository;
        this.wireRepository = wireRepository;
        this.stockRepository = stockRepository;
    }

    public List<OrderResponseDTO> execute(RequestType type, OrderStatus status) {
        // O repositório de domínio agora fornece o contrato para a busca priorizada
        List<OrderQueue> orders = queueRepository.findByStatusAndTypePrioritized(status, type);

        return orders.stream().map(order -> {
            Object details = findDetails(order.getId(), order.getType());
            
            return OrderResponseDTO.builder()
                .id(order.getId())
                .pwNumber(order.getPwNumber())
                .operatorNumber(order.getOperatorNumber())
                .type(order.getType())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .details(details)
                .build();
        }).collect(Collectors.toList());
    }

    private Object findDetails(UUID orderId, RequestType type) {
        return switch (type) {
            case identification_printing -> printingRepository.findByOrderQueueId(orderId).orElse(null);
            case wire_cutting -> wireRepository.findByOrderQueueId(orderId).orElse(null);
            case stock_withdrawal -> stockRepository.findByOrderQueueId(orderId).orElse(null);
        };
    }

    // Nota: O método extractUrgency foi mantido caso você precise dele para lógica extra, 
    // embora a ordenação já venha do SQL.
    private boolean extractUrgency(Object details) {
        if (details instanceof PrintingDetails d) return d.getIsUrgent();
        if (details instanceof WireCuttingDetails d) return d.getIsUrgent();
        if (details instanceof StockWithdrawalDetails d) return d.getIsUrgent();
        return false;
    }
}