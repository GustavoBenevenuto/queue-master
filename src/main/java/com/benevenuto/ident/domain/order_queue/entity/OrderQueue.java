package com.benevenuto.ident.domain.order_queue.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.benevenuto.ident.enums.OrderStatus;
import com.benevenuto.ident.enums.RequestType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_queue")
@Data
@Builder // Habilita o StockWithdrawalDetails.builder()
@NoArgsConstructor // Necessário para o Hibernate
@AllArgsConstructor // Necessário para o Builder funcionar
public class OrderQueue {

    @Id
    private UUID id;

    @Column(name = "pw_number", nullable = false, length = 50)
    private String pwNumber;

    @Column(name = "operator_number", nullable = false, length = 50)
    private String operatorNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.pending;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}