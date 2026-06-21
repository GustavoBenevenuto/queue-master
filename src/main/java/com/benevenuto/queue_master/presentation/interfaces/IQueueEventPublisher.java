package com.benevenuto.queue_master.presentation.interfaces;

import com.benevenuto.queue_master.domain.order_queue.enums.OrderStatus;

public interface IQueueEventPublisher {
    void publishQueueUpdate(OrderStatus status, String operatorNumber);
}