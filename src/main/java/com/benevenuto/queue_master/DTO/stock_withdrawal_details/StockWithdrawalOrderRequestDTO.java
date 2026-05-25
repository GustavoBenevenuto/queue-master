package com.benevenuto.queue_master.DTO.stock_withdrawal_details;

import com.benevenuto.queue_master.DTO.common.BaseOrderRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class StockWithdrawalOrderRequestDTO extends BaseOrderRequestDTO {
	private String itemName;
}