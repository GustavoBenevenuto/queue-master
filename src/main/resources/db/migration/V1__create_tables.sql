CREATE TABLE tb_print_ident (
    -- UUID como chave primária
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Colunas obrigatórias e textos
    content VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    reason TEXT,
    
    -- Boolean e Enums (armazenados como String via @Enumerated)
    is_urgent BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED',
    
    -- Operador e Auditoria
    operator_number VARCHAR(100),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

-- Índices para otimizar a ordenação solicitada no Controller
CREATE INDEX idx_tb_print_ident_urgency_date ON tb_print_ident (is_urgent DESC, created_at DESC);
CREATE INDEX idx_tb_print_ident_operator ON tb_print_ident (operator_number);