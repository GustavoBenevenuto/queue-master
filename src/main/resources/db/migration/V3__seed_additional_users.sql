-- Seed de 5 novos usuários (senha padrão para todos: 'pass123456')
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
) VALUES
    (
        'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a21',
        'Carlos Inventor',
        'carlos.inventor@queuemaster.com',
        1002,
        '$2b$10$R31cuP0ipgGywZPABWjTDunGI0Ryekq9fiJfhtrIrb3v.iU9kJo6.', -- Hash de 'pass123456'
        'INVENTOR',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'b2eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
        'Mariana Operadora',
        'mariana.operadora@queuemaster.com',
        1003,
        '$2b$10$379lcnJx9TxpQJF4A.6mk.5pUjfAXDJbM9QYgotFZUC5zBUZiksB.', -- Hash de 'pass123456'
        'OPERATOR',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'b3eebc99-9c0b-4ef8-bb6d-6bb9bd380a23',
        'Joao Operador',
        'joao.operador@queuemaster.com',
        1004,
        '$2b$10$yB1KfxZ3nz3/.3X7jaQYQO.B05mm3mjGS7t2ICE3LT/.mPMfEoBpe', -- Hash de 'pass123456'
        'OPERATOR',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'b4eebc99-9c0b-4ef8-bb6d-6bb9bd380a24',
        'Patricia Inventora',
        'patricia.inventora@queuemaster.com',
        1005,
        '$2b$10$p/cma5P0hT9Ntm6Adi6Q7eF6/8SO89xaBu4seuYqk8YGNfSM0DUwG', -- Hash de 'pass123456'
        'INVENTOR',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'b5eebc99-9c0b-4ef8-bb6d-6bb9bd380a25',
        'Rafael Operador',
        'rafael.operador@queuemaster.com',
        1006,
        '$2b$10$f2dC0dGH25itveEmLQkezuIkVj3Ma1LHu3BNoYuC6MDr6GOuC9Zz6', -- Hash de 'pass123456'
        'OPERATOR',
        TRUE,
        NOW(),
        NOW()
    );
