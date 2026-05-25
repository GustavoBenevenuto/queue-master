package com.benevenuto.queue_master.DTO.common;

import java.time.LocalDateTime;
import java.util.UUID;

import com.benevenuto.queue_master.enums.OrderStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseOrderResponseDTO {
    private UUID id;
    private String pwNumber;
    private String operatorNumber;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
