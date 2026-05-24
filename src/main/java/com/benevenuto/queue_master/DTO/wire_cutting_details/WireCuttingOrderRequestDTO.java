package com.benevenuto.queue_master.DTO.wire_cutting_details;

import java.math.BigDecimal;
import java.util.List;

import com.benevenuto.queue_master.DTO.common.BaseOrderRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;



@Getter
@SuperBuilder
@NoArgsConstructor
public class WireCuttingOrderRequestDTO extends BaseOrderRequestDTO {
    private List<WireCuttingItemRequestDTO> items;
    
    @Getter
	public class WireCuttingItemRequestDTO {
	    private String wireName;
	    private Integer quantity;
	    private Boolean isUrgent;
	    private String reason;
	    private BigDecimal lengthMm;
	}
}
