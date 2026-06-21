package com.benevenuto.queue_master.presentation.interfaces;

import com.benevenuto.queue_master.domain.common.enums.OrderStatus;

public interface IQueueEventPublisher {
    void publishQueueUpdate(OrderStatus status, String operatorNumber);
}