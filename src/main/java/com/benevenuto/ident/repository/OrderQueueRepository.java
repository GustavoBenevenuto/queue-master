package com.benevenuto.ident.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.benevenuto.ident.entity.OrderQueue;
import com.benevenuto.ident.enums.OrderStatus;
import com.benevenuto.ident.enums.RequestType;

@Repository
public interface OrderQueueRepository extends JpaRepository<OrderQueue, UUID> {

    /**
     * O "Coração" da Fila por Estação:
     * Agora utiliza JOINs para buscar a urgência dentro dos detalhes específicos.
     * A lógica ordena: Urgentes (TRUE) primeiro, depois por data de criação.
     */
    @Query("SELECT o FROM OrderQueue o " +
           "LEFT JOIN PrintingDetails p ON o.id = p.orderQueue.id " +
           "LEFT JOIN WireCuttingDetails w ON o.id = w.orderQueue.id " +
           "LEFT JOIN StockWithdrawalDetails s ON o.id = s.orderQueue.id " +
           "WHERE o.status = :status AND o.type = :type " +
           "ORDER BY " +
           "  CASE " +
           "    WHEN o.type = 'identification_printing' THEN p.isUrgent " +
           "    WHEN o.type = 'wire_cutting' THEN w.isUrgent " +
           "    WHEN o.type = 'stock_withdrawal' THEN s.isUrgent " +
           "    ELSE false " +
           "  END DESC, o.createdAt ASC")
    List<OrderQueue> findByStatusAndTypePrioritized(
        @Param("status") OrderStatus status, 
        @Param("type") RequestType type
    );

    // Busca geral por tipo com a mesma lógica de prioridade
    @Query("SELECT o FROM OrderQueue o " +
           "LEFT JOIN PrintingDetails p ON o.id = p.orderQueue.id " +
           "LEFT JOIN WireCuttingDetails w ON o.id = w.orderQueue.id " +
           "LEFT JOIN StockWithdrawalDetails s ON o.id = s.orderQueue.id " +
           "WHERE o.type = :type " +
           "ORDER BY " +
           "  CASE " +
           "    WHEN o.type = 'identification_printing' THEN p.isUrgent " +
           "    WHEN o.type = 'wire_cutting' THEN w.isUrgent " +
           "    WHEN o.type = 'stock_withdrawal' THEN s.isUrgent " +
           "    ELSE false " +
           "  END DESC, o.createdAt ASC")
    List<OrderQueue> findByTypePrioritized(@Param("type") RequestType type);

    // Busca pedidos por número da PW (Simples, sem necessidade de Join)
    List<OrderQueue> findByPwNumber(String pwNumber);

    // Conta quantos pedidos de um tipo específico estão na fila
    long countByStatusAndType(OrderStatus status, RequestType type);
}