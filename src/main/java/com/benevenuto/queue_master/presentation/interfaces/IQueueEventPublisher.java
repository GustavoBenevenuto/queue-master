package com.benevenuto.queue_master.presentation.interfaces;

import com.benevenuto.queue_master.domain.common.HasOperatorNumber;
import com.benevenuto.queue_master.domain.common.enums.QueueEventType;

public interface IQueueEventPublisher<T extends HasOperatorNumber> {
    void publishQueueUpdate(QueueEventType type, T order);
}
