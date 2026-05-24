package com.benevenuto.queue_master.DTO;

import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;

public record OrderDataNotificationDTO(RequestType type, OrderStatus status, String operatorNumber) {}