package com.benevenuto.queue_master.domain.wire_cutting_details.repository;

import java.util.UUID;

import com.benevenuto.queue_master.domain.common.repository.IBaseRepositoryOrder;
import com.benevenuto.queue_master.domain.wire_cutting_details.entity.WireCuttingDetails;

public interface IWireCuttingDetailsRepository extends IBaseRepositoryOrder<WireCuttingDetails, UUID> {}