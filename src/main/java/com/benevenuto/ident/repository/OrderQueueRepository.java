package com.benevenuto.ident.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benevenuto.ident.entity.OrderQueue;
import com.benevenuto.ident.enums.OrderStatus;
import com.benevenuto.ident.enums.RequestType;

@Repository
public interface OrderQueueRepository extends JpaRepository<OrderQueue, UUID> {

    /**
     * O "Coração" da Fila por Estação:
     * Filtra por STATUS (ex: pending) e TIPO (ex: wire_cutting).
     * Ordena por Urgência (TRUE primeiro) e depois por Data de Criação (mais antigos primeiro).
     */
    List<OrderQueue> findByStatusAndTypeOrderByIsUrgentDescCreatedAtAsc(OrderStatus status, RequestType type);

    // Busca geral por tipo (independente de status) ordenado por prioridade
    List<OrderQueue> findByTypeOrderByIsUrgentDescCreatedAtAsc(RequestType type);

    // Busca pedidos por número da PW
    List<OrderQueue> findByPwNumber(String pwNumber);

    // Conta quantos pedidos de um tipo específico estão na fila
    long countByStatusAndType(OrderStatus status, RequestType type);
}