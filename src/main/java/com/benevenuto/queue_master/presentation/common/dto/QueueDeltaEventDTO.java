package com.benevenuto.queue_master.presentation.common.dto;

import com.benevenuto.queue_master.domain.common.HasOperatorNumber;
import com.benevenuto.queue_master.domain.common.enums.QueueEventType;

/**
 * Payload enviado via WebSocket a cada mudança na fila. Carrega o objeto completo da ordem
 * afetada (não a lista inteira) — o cliente já mantém a lista carregada via REST paginado e
 * usa esse evento para inserir/atualizar/remover localmente apenas o item correspondente.
 */
public record QueueDeltaEventDTO<T extends HasOperatorNumber>(QueueEventType type, T data) {}
