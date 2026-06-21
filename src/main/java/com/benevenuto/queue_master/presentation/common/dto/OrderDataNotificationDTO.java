package com.benevenuto.queue_master.presentation.common.dto;

import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

public record OrderDataNotificationDTO(OrderStatus status, String operatorNumber) {}