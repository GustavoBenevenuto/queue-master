package com.benevenuto.queue_master.presentation.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseOrderRequestDTO {
    private String workOrderNumber;
    private String operatorNumber;
    private Integer quantity;
    private Boolean isUrgent;
    private String reason;
}