package com.benevenuto.queue_master.presentation.printing.dto;

import com.benevenuto.queue_master.presentation.common.dto.BaseOrderRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class PrintingOrderRequestDTO extends BaseOrderRequestDTO {
	private String printText;
}