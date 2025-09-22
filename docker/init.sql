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