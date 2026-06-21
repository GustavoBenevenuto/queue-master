package com.benevenuto.queue_master.domain.wire_cutting_details.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.benevenuto.queue_master.domain.common.enums.OrderStatus;
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
@Table(name = "wire_cutting_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WireCuttingDetails {

    @Id
    private UUID id;

    @Column(name = "work_order_number", nullable = false, length = 50)
    private String workOrderNumber;

    @Column(name = "operator_number", nullable = false, length = 50)
    private String operatorNumber;

    @Column(name = "wire_name", nullable = false, length = 100)
    private String wireName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "is_urgent")
    private Boolean isUrgent = false;

    @Column(name = "length_mm", nullable = false, precision = 10, scale = 2)
    private BigDecimal lengthMm;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

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