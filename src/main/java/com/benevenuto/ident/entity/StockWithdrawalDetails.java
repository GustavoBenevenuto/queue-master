package com.benevenuto.ident.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_withdrawal_details")
@Data
@Builder // Habilita o StockWithdrawalDetails.builder()
@NoArgsConstructor // Necessário para o Hibernate
@AllArgsConstructor // Necessário para o Builder funcionar
public class StockWithdrawalDetails {

    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_queue_id", nullable = false)
    private OrderQueue orderQueue;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "is_urgent")
    private Boolean isUrgent = false;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;
}
