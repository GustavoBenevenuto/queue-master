package com.benevenuto.queue_master.DTO.stock_withdrawal_details;

import java.math.BigDecimal;
import java.util.List;

import com.benevenuto.queue_master.DTO.common.BaseOrderRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@NoArgsConstructor
public class StockWithdrawalOrderRequestDTO extends BaseOrderRequestDTO {
    private List<StockWithdrawalItemRequestDTO> items;
    
    @Getter
	public class StockWithdrawalItemRequestDTO {
	    private String itemName;
	    private BigDecimal lenghtMm;
	    private Integer quantity;
	    private Boolean isUrgent;
	    private String reason;
	}
}
