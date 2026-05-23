package com.benevenuto.queue_master.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

import com.benevenuto.queue_master.enums.OrderStatus;
import com.benevenuto.queue_master.enums.RequestType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Oculta campos nulos no JSON final
public class OrderResponseDTO {

    private UUID id;
    private String pwNumber;
    private String operatorNumber;
    private RequestType type;
    private OrderStatus status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Este campo receberá o objeto de detalhes correspondente:
     * PrintingDetails, WireCuttingDetails ou StockWithdrawalDetails.
     */
    private Object details;
}