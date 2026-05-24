package com.benevenuto.queue_master.DTO.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseOrderRequestDTO {
    private String pwNumber;
    private String operatorNumber;
}