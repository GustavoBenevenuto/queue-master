package com.benevenuto.ident.useCase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benevenuto.ident.DTO.OrderResponseDTO;
import com.benevenuto.ident.entity.OrderQueue;
import com.benevenuto.ident.entity.PrintingDetails;
import com.benevenuto.ident.entity.StockWithdrawalDetails;
import com.benevenuto.ident.entity.WireCuttingDetails;
import com.benevenuto.ident.enums.OrderStatus;
import com.benevenuto.ident.enums.RequestType;
import com.benevenuto.ident.repository.OrderQueueRepository;
import com.benevenuto.ident.repository.PrintingDetailsRepository;
import com.benevenuto.ident.repository.StockWithdrawalDetailsRepository;
import com.benevenuto.ident.repository.WireCuttingDetailsRepository;

@Service
public class GetQueueByStationUseCase {

    @Autowired
    private OrderQueueRepository queueRepository;
    
    @Autowired
    private PrintingDetailsRepository printingRepository;
    
    @Autowired
    private WireCuttingDetailsRepository wireRepository;
    
    @Autowired
    private StockWithdrawalDetailsRepository stockRepository;

    public List<OrderResponseDTO> execute(RequestType type, OrderStatus status) {
        // 1. Chamada corrigida: Usando o método com @Query que criamos no Repository
        // Este método já retorna os dados ordenados por Urgência e Data direto do SQL
        List<OrderQueue> orders = queueRepository.findByStatusAndTypePrioritized(status, type);

        // 2. Mapeia para o DTO
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
        
        // Removido o .sorted() manual, pois o SQL já cuidou disso via JOIN
    }

    private Object findDetails(UUID orderId, RequestType type) {
        return switch (type) {
            case identification_printing -> printingRepository.findByOrderQueueId(orderId).orElse(null);
            case wire_cutting -> wireRepository.findByOrderQueueId(orderId).orElse(null);
            case stock_withdrawal -> stockRepository.findByOrderQueueId(orderId).orElse(null);
        };
    }

    private boolean extractUrgency(Object details) {
        if (details instanceof PrintingDetails d) return d.getIsUrgent();
        if (details instanceof WireCuttingDetails d) return d.getIsUrgent();
        if (details instanceof StockWithdrawalDetails d) return d.getIsUrgent();
        return false;
    }
}