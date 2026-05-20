package com.benevenuto.ident.entity;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.benevenuto.ident.enums.PrintIdentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_print_ident")
@Data
@EntityListeners(AuditingEntityListener.class) // Habilita o preenchimento automático de datas
public class PrintIdent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "is_urgent", nullable = false)
    private Boolean isUrgent = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrintIdentStatus status = PrintIdentStatus.NOT_STARTED;

    @Column(name = "operator_number")
    private String operatorNumber;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
