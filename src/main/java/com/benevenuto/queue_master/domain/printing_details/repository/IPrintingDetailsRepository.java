package com.benevenuto.queue_master.domain.printing_details.repository;

import java.util.UUID;

import com.benevenuto.queue_master.domain.common.repository.IBaseRepositoryOrder;
import com.benevenuto.queue_master.domain.printing_details.entity.PrintingDetails;

public interface IPrintingDetailsRepository extends IBaseRepositoryOrder<PrintingDetails, UUID> {}