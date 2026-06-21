package com.benevenuto.queue_master.presentation.common.dto;

import com.benevenuto.queue_master.domain.order_queue.enums.OrderStatus;

public record OrderDataNotificationDTO(OrderStatus status, String operatorNumber) {}