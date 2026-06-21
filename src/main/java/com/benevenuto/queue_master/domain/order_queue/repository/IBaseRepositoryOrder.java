package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.List;

public interface IBaseRepositoryOrder<T, ID> extends IBaseRepository<T, ID> {
    List<T> findByOperatorNumber(String operatorNumber);
    List<T> findByWorkOrderNumber(String workOrderNumber);
}