package com.benevenuto.queue_master.DTO.pritting_details;

import com.benevenuto.queue_master.DTO.common.BaseOrderRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class PrintingOrderRequestDTO extends BaseOrderRequestDTO {
	private String printText;
    private Integer quantity;
    private Boolean isUrgent;
    private String reason;
}