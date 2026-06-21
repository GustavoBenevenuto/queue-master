package com.benevenuto.queue_master.infra.wire_cutting_details.repository.jpa;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.wire_cutting_details.entity.WireCuttingDetails;
import com.benevenuto.queue_master.domain.wire_cutting_details.repository.IWireCuttingDetailsRepository;
import com.benevenuto.queue_master.infra.common.repository.jpa.interfaces.BaseRepositoryOrderJpaImpl;
import com.benevenuto.queue_master.infra.wire_cutting_details.repository.jpa.interfaces.IWireCuttingDetailsJpaRepository;

@Component
public class WireCuttingDetailsRepositoryJpaImpl 
       extends BaseRepositoryOrderJpaImpl<WireCuttingDetails, UUID, IWireCuttingDetailsJpaRepository> 
       implements IWireCuttingDetailsRepository {

    public WireCuttingDetailsRepositoryJpaImpl(IWireCuttingDetailsJpaRepository repository) {
        super(repository, WireCuttingDetails.class);
    }
}