package com.benevenuto.queue_master.DTO;

import com.benevenuto.queue_master.enums.OrderStatus;

public record OrderDataNotificationDTO(OrderStatus status, String operatorNumber) {}