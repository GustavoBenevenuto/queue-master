package com.benevenuto.queue_master.presentation.stock_withdrawal.dto;

import com.benevenuto.queue_master.presentation.common.dto.BaseOrderRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class StockWithdrawalOrderRequestDTO extends BaseOrderRequestDTO {
	private String itemName;
}