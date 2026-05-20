-- 1. Tabela Principal
CREATE TABLE tb_print_ident (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quantity INTEGER NOT NULL,
    reason TEXT,
    is_urgent BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED',
    operator_number VARCHAR(100),
    -- Adicionado DEFAULT aqui:
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

-- 2. Tabela de Conteúdos (Array de Strings)
CREATE TABLE tb_print_ident_content (
    print_ident_id UUID NOT NULL,
    content_item TEXT NOT NULL,
    CONSTRAINT fk_print_ident FOREIGN KEY (print_ident_id) 
        REFERENCES tb_print_ident (id) ON DELETE CASCADE
);

-- Índices para performance
CREATE INDEX idx_tb_print_ident_urgency ON tb_print_ident (is_urgent DESC, created_at DESC);
CREATE INDEX idx_content_parent_id ON tb_print_ident_content (print_ident_id);