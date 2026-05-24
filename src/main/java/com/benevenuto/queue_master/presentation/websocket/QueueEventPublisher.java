package com.benevenuto.queue_master.presentation.websocket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.application.printing_details.GetPrintingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.stock_withdrawal_details.GetStockWithdrawalOrdersByOperatorUseCase;
import com.benevenuto.queue_master.application.wire_cutting_details.GetWireCuttingOrdersByOperatorUseCase;
import com.benevenuto.queue_master.domain.order_queue.entity.PrintingDetails;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

@Component
public class QueueEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    
    // Casos de Uso do Operador por Subdomínio
    private final GetPrintingOrdersByOperatorUseCase getPrintingOrdersByOperatorUseCase;
    private final GetWireCuttingOrdersByOperatorUseCase getWireCuttingOrdersByOperatorUseCase;
    private final GetStockWithdrawalOrdersByOperatorUseCase getStockWithdrawalOrdersByOperatorUseCase;

    public QueueEventPublisher(
            SimpMessagingTemplate messagingTemplate, 
            GetPrintingOrdersByOperatorUseCase getPrintingOrdersByOperatorUseCase,
            GetWireCuttingOrdersByOperatorUseCase getWireCuttingOrdersByOperatorUseCase,
            GetStockWithdrawalOrdersByOperatorUseCase getStockWithdrawalOrdersByOperatorUseCase
    ) {
        this.messagingTemplate = messagingTemplate;
        this.getPrintingOrdersByOperatorUseCase = getPrintingOrdersByOperatorUseCase;
        this.getWireCuttingOrdersByOperatorUseCase = getWireCuttingOrdersByOperatorUseCase;
        this.getStockWithdrawalOrdersByOperatorUseCase = getStockWithdrawalOrdersByOperatorUseCase;
    }

    /**
     * Publica atualizações no painel da estação específica da fábrica.
     * Como a listagem da fila geral por estação agora roda nos controllers específicos,
     * o WebSocket apenas repassa o payload processado ou dispara gatilhos simples se necessário.
     */
    public void publishQueueUpdate(RequestType type, OrderStatus status) {
        // Exemplo de rota: /topic/queue/wire_cutting/pending
        String destination = String.format("/topic/queue/%s/%s", type.name(), status.name());
        
        // Se você optar por enviar a fila atualizada diretamente por aqui, 
        // certifique-se de injetar também os UseCases de fila (GetPrintingQueueUseCase, etc.) 
        // e fazer um switch(type) para buscar a lista antes de enviar.
        messagingTemplate.convertAndSend(destination, "Fila atualizada"); 
    }
    
    /**
     * Consolida em tempo real todas as ordens das 3 estações que pertencem a este operador.
     */
    public void publishOperatorUpdate(String operatorNumber) {
        List<PrintingDetails> consolidatedQueue = new ArrayList<>();

        // Coleta os dados isolados de cada novo Caso de Uso correspondente
        consolidatedQueue.addAll(getPrintingOrdersByOperatorUseCase.execute(operatorNumber));
//        consolidatedQueue.addAll(getWireCuttingOrdersByOperatorUseCase.execute(operatorNumber));
//        consolidatedQueue.addAll(getStockWithdrawalOrdersByOperatorUseCase.execute(operatorNumber));

        // Garante que o payload enviado via WS siga ordenado de forma decrescente pela data de criação
        List<Object> updatedOperatorOrders = consolidatedQueue.stream()
                .sorted(Comparator.comparing(PrintingDetails::getCreatedAt).reversed())
                .collect(Collectors.toList());
        
        // Envia para o tópico exclusivo dele. Ex: /topic/operator/OP-TESTE
        String destination = "/topic/operator/" + operatorNumber;
        messagingTemplate.convertAndSend(destination, updatedOperatorOrders);
    }
}