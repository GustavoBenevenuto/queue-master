package com.benevenuto.queue_master.infra.order_queue.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.order_queue.entity.OrderQueue;
import com.benevenuto.queue_master.domain.order_queue.repository.IOrderQueueRepository;
import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;
import com.benevenuto.queue_master.infra.common.repository.jpa.interfaces.BaseRepositoryJpaImpl;
import com.benevenuto.queue_master.infra.order_queue.repository.jpa.interfaces.IOrderQueueJpaRepository;


@Component
public class OrderQueueRepositoryJpaImpl extends BaseRepositoryJpaImpl<OrderQueue, UUID, IOrderQueueJpaRepository>
		implements IOrderQueueRepository {

	public OrderQueueRepositoryJpaImpl(IOrderQueueJpaRepository repository) {
		super(repository);
	}

	@Override
	public List<OrderQueue> findByStatusAndTypePrioritized(OrderStatus status, RequestType type) {
		return repository.findByStatusAndTypePrioritized(status, type);
	}

	@Override
	public List<OrderQueue> findByTypePrioritized(RequestType type) {
		return repository.findByTypePrioritized(type);
	}

	@Override
	public List<OrderQueue> findByPwNumber(String pwNumber) {
		return repository.findByPwNumber(pwNumber);
	}
	
	@Override
	public List<OrderQueue> findByOperatorNumber(String operatorNumber) {
	    return repository.findByOperatorNumberPrioritized(operatorNumber);
	}

	@Override
	public long countByStatusAndType(OrderStatus status, RequestType type) {
		return repository.countByStatusAndType(status, type);
	}
}