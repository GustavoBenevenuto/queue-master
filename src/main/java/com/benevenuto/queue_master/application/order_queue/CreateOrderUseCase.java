package com.benevenuto.queue_master.application.order_queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.DTO.OrderRequestDTO;
import com.benevenuto.queue_master.DTO.OrderDataNotificationDTO;
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

import jakarta.transaction.Transactional;

public class CreateOrderUseCase {

    private final IOrderQueueRepository queueRepository;
    private final IPrintingDetailsRepository printingRepository;
    private final IWireCuttingDetailsRepository wireRepository;
    private final IStockWithdrawalDetailsRepository stockRepository;

    public CreateOrderUseCase(
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

    @Transactional
    public List<OrderDataNotificationDTO> execute(OrderRequestDTO dto) {
        // Lista que acumulará as notificações de tudo o que foi criado
        List<OrderDataNotificationDTO> notifications = new ArrayList<>();

        Optional.ofNullable(dto.getPrinting()).ifPresent(list -> 
            list.forEach(item -> {
                savePrinting(dto, item);
                notifications.add(new OrderDataNotificationDTO(RequestType.identification_printing, OrderStatus.pending));
            }));
        
        Optional.ofNullable(dto.getWireCutting()).ifPresent(list -> 
            list.forEach(item -> {
                saveWire(dto, item);
                notifications.add(new OrderDataNotificationDTO(RequestType.wire_cutting, OrderStatus.pending));
            }));
        
        Optional.ofNullable(dto.getStockWithdrawal()).ifPresent(list -> 
            list.forEach(item -> {
                saveStock(dto, item);
                notifications.add(new OrderDataNotificationDTO(RequestType.stock_withdrawal, OrderStatus.pending));
            }));

        // Retorna a lista contendo mapeado tudo o que de fato entrou no banco
        return notifications;
    }

    private void savePrinting(OrderRequestDTO dto, OrderRequestDTO.PrintingItemDTO item) {
        OrderQueue queue = createBase(dto, RequestType.identification_printing);
        printingRepository.save(PrintingDetails.builder()
            .id(UUID.randomUUID())
            .orderQueue(queue)
            .printText(item.getPrintText())
            .quantity(item.getQuantity())
            .isUrgent(item.getIsUrgent())
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
            .isUrgent(item.getIsUrgent())
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
            .isUrgent(item.getIsUrgent())
            .reason(item.getReason())
            .build());
    }

    private OrderQueue createBase(OrderRequestDTO dto, RequestType type) {
        return queueRepository.save(OrderQueue.builder()
            .id(UUID.randomUUID())
            .pwNumber(dto.getPwNumber())
            .operatorNumber(dto.getOperatorNumber())
            .type(type)
            .status(OrderStatus.pending)
            .build());
    }
}