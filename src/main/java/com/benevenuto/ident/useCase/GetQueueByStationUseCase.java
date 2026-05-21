package com.benevenuto.ident.useCase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benevenuto.ident.DTO.OrderResponseDTO;
import com.benevenuto.ident.entity.OrderQueue;
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
        // 1. Busca as ordens básicas na fila
        List<OrderQueue> orders = queueRepository.findByStatusAndTypeOrderByIsUrgentDescCreatedAtAsc(status, type);

        // 2. Mapeia cada Ordem para um OrderResponseDTO incluindo seus detalhes
        return orders.stream().map(order -> {
            Object details = findDetails(order.getId(), order.getType());
            
            return OrderResponseDTO.builder()
                .id(order.getId())
                .pwNumber(order.getPwNumber())
                .operatorNumber(order.getOperatorNumber())
                .type(order.getType())
                .status(order.getStatus())
                .isUrgent(order.getIsUrgent())
                .createdAt(order.getCreatedAt())
                .details(details) // Acopla o detalhe técnico aqui
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
}