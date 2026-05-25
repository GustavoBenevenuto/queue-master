package com.benevenuto.queue_master.presentation.websocket;

import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersUseCase;
import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

@Component
public class QueueEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    
    // Use Cases para listagem GERAL (Todos os itens da fila - @GetMapping)
    private final GetStockWithdrawalOrdersUseCase getStockWithdrawalOrdersUseCase;
    private final GetPrintingOrdersUseCase getPrintingOrdersUseCase;
    private final GetWireCuttingOrdersUseCase getWireCuttingOrdersUseCase;

    // Use Cases para listagem por OPERADOR (@GetMapping("/operator/{operatorNumber}"))
    private final GetStockWithdrawalOrdersByOperatorUseCase getStockWithdrawalByOperatorUseCase;
    private final GetPrintingOrdersByOperatorUseCase getPrintingByOperatorUseCase;
    private final GetWireCuttingOrdersByOperatorUseCase getWireCuttingByOperatorUseCase;

    public QueueEventPublisher(
            SimpMessagingTemplate messagingTemplate, 
            GetStockWithdrawalOrdersUseCase getStockWithdrawalOrdersUseCase,
            GetPrintingOrdersUseCase getPrintingOrdersUseCase,
            GetWireCuttingOrdersUseCase getWireCuttingOrdersUseCase,
            GetStockWithdrawalOrdersByOperatorUseCase getStockWithdrawalByOperatorUseCase,
            GetPrintingOrdersByOperatorUseCase getPrintingByOperatorUseCase,
            GetWireCuttingOrdersByOperatorUseCase getWireCuttingByOperatorUseCase
    ) {
        this.messagingTemplate = messagingTemplate;
        this.getStockWithdrawalOrdersUseCase = getStockWithdrawalOrdersUseCase;
        this.getPrintingOrdersUseCase = getPrintingOrdersUseCase;
        this.getWireCuttingOrdersUseCase = getWireCuttingOrdersUseCase;
        this.getStockWithdrawalByOperatorUseCase = getStockWithdrawalByOperatorUseCase;
        this.getPrintingByOperatorUseCase = getPrintingByOperatorUseCase;
        this.getWireCuttingByOperatorUseCase = getWireCuttingByOperatorUseCase;
    }

    /**
     * Atualiza em tempo real a fila GERAL da estação E a fila específica do OPERADOR.
     */
    public void publishQueueUpdate(RequestType type, OrderStatus status, String operatorNumber) {
        // 1. Resolve o nome base do tópico por subdomínio (Kebab-Case)
        String topicBase = switch (type) {
            case identification_printing -> "printing";
            case wire_cutting -> "wire-cutting";
            case stock_withdrawal -> "stock-withdrawal";
        };

        // 2. DISPARO GERAL: Atualiza quem está ouvindo a fila cheia (findAll)
        List<?> updatedGeneralQueue = switch (type) {
            case identification_printing -> getPrintingOrdersUseCase.execute();
            case wire_cutting -> getWireCuttingOrdersUseCase.execute();
            case stock_withdrawal -> getStockWithdrawalOrdersUseCase.execute();
        };
        String generalDestination = "/topic/" + topicBase;
        messagingTemplate.convertAndSend(generalDestination, updatedGeneralQueue);

        // 3. DISPARO POR OPERADOR: Atualiza o painel privado daquele operador específico na estação
        if (operatorNumber != null && !operatorNumber.isBlank()) {
            List<?> updatedOperatorQueue = switch (type) {
                case identification_printing -> getPrintingByOperatorUseCase.execute(operatorNumber);
                case wire_cutting -> getWireCuttingByOperatorUseCase.execute(operatorNumber);
                case stock_withdrawal -> getStockWithdrawalByOperatorUseCase.execute(operatorNumber);
            };
            String operatorDestination = String.format("/topic/%s/operator/%s", topicBase, operatorNumber);
            messagingTemplate.convertAndSend(operatorDestination, updatedOperatorQueue);
        }
    }
}