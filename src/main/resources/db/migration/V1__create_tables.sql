CREATE TABLE printing_details (
    id UUID PRIMARY KEY,
    work_order_number VARCHAR(50) NOT NULL,
    operator_number VARCHAR(50) NOT NULL,
    print_text TEXT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    is_urgent BOOLEAN DEFAULT FALSE,
    reason TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE wire_cutting_details (
    id UUID PRIMARY KEY,
    work_order_number VARCHAR(50) NOT NULL,
    operator_number VARCHAR(50) NOT NULL,
    wire_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    is_urgent BOOLEAN DEFAULT FALSE,
    length_mm DECIMAL(10, 2) NOT NULL CHECK (length_mm > 0),
    reason TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE stock_withdrawal_details (
    id UUID PRIMARY KEY,
    work_order_number VARCHAR(50) NOT NULL,
    operator_number VARCHAR(50) NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    is_urgent BOOLEAN DEFAULT FALSE,
    reason TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

