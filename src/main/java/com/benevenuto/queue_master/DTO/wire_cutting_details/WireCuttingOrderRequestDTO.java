package com.benevenuto.queue_master.DTO.wire_cutting_details;

import java.math.BigDecimal;

import com.benevenuto.queue_master.DTO.common.BaseOrderRequestDTO;

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