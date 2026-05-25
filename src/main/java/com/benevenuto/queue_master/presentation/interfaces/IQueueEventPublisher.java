package com.benevenuto.queue_master.presentation.interfaces;

import com.benevenuto.queue_master.enums.OrderStatus;

public interface IQueueEventPublisher {
    void publishQueueUpdate(OrderStatus status, String operatorNumber);
}