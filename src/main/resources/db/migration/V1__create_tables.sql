CREATE TABLE order_queue (
    id UUID PRIMARY KEY,
    pw_number VARCHAR(50) NOT NULL,
    operator_number VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL, -- Removido ENUM, agora String
    type VARCHAR(50) NOT NULL,   -- Removido ENUM, agora String
    is_urgent BOOLEAN DEFAULT FALSE,
    -- Registra o momento exato com o fuso horário (UTC no banco por padrão)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Índice para busca rápida de pedidos pendentes e urgentes na fila
CREATE INDEX idx_order_queue_workflow ON order_queue(status, is_urgent, created_at);

CREATE TABLE printing_details (
    id UUID PRIMARY KEY,
    order_queue_id UUID NOT NULL REFERENCES order_queue(id) ON DELETE CASCADE,
    print_text TEXT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    reason TEXT NOT NULL
);

CREATE TABLE wire_cutting_details (
    id UUID PRIMARY KEY,
    order_queue_id UUID NOT NULL REFERENCES order_queue(id) ON DELETE CASCADE,
    wire_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    length_mm DECIMAL(10, 2) NOT NULL CHECK (length_mm > 0)
);

CREATE TABLE stock_withdrawal_details (
    id UUID PRIMARY KEY,
    order_queue_id UUID NOT NULL REFERENCES order_queue(id) ON DELETE CASCADE,
    item_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    reason TEXT NOT NULL
);
