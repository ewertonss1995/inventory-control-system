CREATE TABLE IF NOT EXISTS categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    registration_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    product_description VARCHAR(100) NOT NULL UNIQUE,
    unit_price NUMERIC NOT NULL,
    quantity INT NOT NULL,
    total_price NUMERIC NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP,
    category_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY,
    user_name VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles (
    user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(role_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO categories (category_name, registration_date, update_date)
VALUES
   ('MODA', NOW(), NULL),
   ('ELETRÔNICOS', NOW(), NULL),
   ('MOVEIS', NOW(), NULL) ON CONFLICT (category_name) DO NOTHING;


INSERT INTO roles (role_id, role_name)
VALUES
    (1, 'ADMIN'),
    (2, 'BASIC') ON CONFLICT (role_name) DO NOTHING;

INSERT INTO products (product_name, product_description, unit_price, quantity, total_price, registration_date, update_date, category_id) VALUES
('Produto A', 'Descrição do Produto A', 19.99, 100, 1999.00, NOW(), NOW(), 1),
('Produto B', 'Descrição do Produto B', 29.99, 50, 1499.50, NOW(), NOW(), 1),
('Produto C', 'Descrição do Produto C', 39.99, 75, 2999.25, NOW(), NOW(), 2),
('Produto D', 'Descrição do Produto D', 49.99, 20, 999.80, NOW(), NOW(), 2),
('Produto E', 'Descrição do Produto E', 59.99, 30, 1799.70, NOW(), NOW(), 3),
('Produto F', 'Descrição do Produto F', 69.99, 10, 699.90, NOW(), NOW(), 3),
('Produto G', 'Descrição do Produto G', 79.99, 15, 1199.85, NOW(), NOW(), 1),
('Produto H', 'Descrição do Produto H', 89.99, 5, 449.95, NOW(), NOW(), 2),
('Produto I', 'Descrição do Produto I', 99.99, 25, 2499.75, NOW(), NOW(), 3),
('Produto J', 'Descrição do Produto J', 109.99, 8, 879.92, NOW(), NOW(), 1);