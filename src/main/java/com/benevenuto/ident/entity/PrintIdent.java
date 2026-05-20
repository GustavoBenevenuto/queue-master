package com.benevenuto.ident.entity;

import com.benevenuto.ident.enums.PrintIdentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_print_ident")
@Data
public class PrintIdent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    // Configuração da segunda tabela (tb_print_ident_content)
    @ElementCollection
    @CollectionTable(
        name = "tb_print_ident_content", 
        joinColumns = @JoinColumn(name = "print_ident_id")
    )
    @Column(name = "content_item")
    private List<String> content;

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
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}