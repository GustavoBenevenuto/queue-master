package com.benevenuto.queue_master.presentation.wire_cutting.dto;

import java.math.BigDecimal;

import com.benevenuto.queue_master.presentation.common.dto.BaseOrderRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class WireCuttingOrderRequestDTO extends BaseOrderRequestDTO {
    private String wireName;
    private BigDecimal lengthMm;
}