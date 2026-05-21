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
@Table(name = "printing_details")
@Data
@Builder // Habilita o StockWithdrawalDetails.builder()
@NoArgsConstructor // Necessário para o Hibernate
@AllArgsConstructor // Necessário para o Builder funcionar
public class PrintingDetails {

    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_queue_id", nullable = false)
    private OrderQueue orderQueue;

    @Column(name = "print_text", nullable = false, columnDefinition = "TEXT")
    private String printText;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "is_urgent")
    private Boolean isUrgent = false;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;
}