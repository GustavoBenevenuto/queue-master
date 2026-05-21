package com.benevenuto.ident.useCase;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benevenuto.ident.DTO.OrderRequestDTO;
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

import jakarta.transaction.Transactional;

@Service
public class CreateOrderUseCase {

    @Autowired
    private OrderQueueRepository queueRepository;
    
    @Autowired
    private PrintingDetailsRepository printingRepository;
    
    @Autowired
    private WireCuttingDetailsRepository wireRepository;
    
    @Autowired
    private StockWithdrawalDetailsRepository stockRepository;

    @Transactional
    public void execute(OrderRequestDTO dto) {
        Optional.ofNullable(dto.getPrinting()).ifPresent(list -> 
            list.forEach(item -> savePrinting(dto, item)));
        
        Optional.ofNullable(dto.getWireCutting()).ifPresent(list -> 
            list.forEach(item -> saveWire(dto, item)));
        
        Optional.ofNullable(dto.getStockWithdrawal()).ifPresent(list -> 
            list.forEach(item -> saveStock(dto, item)));
    }

    private void savePrinting(OrderRequestDTO dto, OrderRequestDTO.PrintingItemDTO item) {
        OrderQueue queue = createBase(dto, RequestType.identification_printing);
        printingRepository.save(PrintingDetails.builder()
            .id(UUID.randomUUID())
            .orderQueue(queue)
            .printText(item.getPrintText())
            .quantity(item.getQuantity())
            .reason(item.getReason())
            .build());
    }

    private void saveWire(OrderRequestDTO dto, OrderRequestDTO.WireItemDTO item) {
        OrderQueue queue = createBase(dto, RequestType.wire_cutting);
        wireRepository.save(WireCuttingDetails.builder()
            .id(UUID.randomUUID())
            .orderQueue(queue)
            .wireName(item.getWireName())
            .quantity(item.getQuantity())
            .lengthMm(item.getLengthMm())
            .build());
    }

    private void saveStock(OrderRequestDTO dto, OrderRequestDTO.StockItemDTO item) {
        OrderQueue queue = createBase(dto, RequestType.stock_withdrawal);
        stockRepository.save(StockWithdrawalDetails.builder()
            .id(UUID.randomUUID())
            .orderQueue(queue)
            .itemName(item.getItemName())
            .quantity(item.getQuantity())
            .reason(item.getReason())
            .build());
    }

    private OrderQueue createBase(OrderRequestDTO dto, RequestType type) {
        return queueRepository.save(OrderQueue.builder()
            .id(UUID.randomUUID())
            .pwNumber(dto.getPwNumber())
            .operatorNumber(dto.getOperatorNumber())
            .type(type)
            .isUrgent(dto.getIsUrgent())
            .status(OrderStatus.pending)
            .build());
    }
}
