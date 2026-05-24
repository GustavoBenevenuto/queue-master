CREATE TABLE "user" (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE, -- Campo de e-mail adicionado
    operator_number INTEGER NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Índice extra para buscas rápidas por e-mail (comum no login)
CREATE INDEX idx_user_email ON "user"(email);

INSERT INTO "user" (
    id, 
    name, 
    email, 
    operator_number, 
    password, 
    role, 
    active, 
    created_at, 
    updated_at
) VALUES (
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'Administrator', 
    'admin@queuemaster.com', 
    1001, 
    '$2a$10$W6k6pakznoZt5l0xSzF1yuOQxnWxmSjHj2puPxbS3bhKWUCwX9zx2', -- Hash de 'admin123'
    'ADMIN', -- O nome do Enum exato
    TRUE, 
    NOW(), 
    NOW()
);