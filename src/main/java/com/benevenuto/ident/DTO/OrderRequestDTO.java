package com.benevenuto.ident.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private String pwNumber;
    private String operatorNumber;
    private List<PrintingItemDTO> printing;
    private List<WireItemDTO> wireCutting;
    private List<StockItemDTO> stockWithdrawal;

    @Data public static class PrintingItemDTO {
        private String printText;
        private Integer quantity;
        private String reason;
        private Boolean isUrgent;
    }

    @Data public static class WireItemDTO {
        private String wireName;
        private Integer quantity;
        private BigDecimal lengthMm;
        private Boolean isUrgent;
    }

    @Data public static class StockItemDTO {
        private String itemName;
        private Integer quantity;
        private String reason;
        private Boolean isUrgent;
    }
}