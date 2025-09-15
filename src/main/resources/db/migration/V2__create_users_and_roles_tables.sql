INSERT INTO categories (category_name, registration_date, update_date)
VALUES
   ('MODA', NOW(), NULL),
   ('ELETRÔNICOS', NOW(), NULL),
   ('MOVEIS', NOW(), NULL) ON CONFLICT (category_name) DO NOTHING;

CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE
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

INSERT INTO roles (role_id, role_name)
VALUES
    (1, 'admin'),
    (2, 'basic') ON CONFLICT (role_name) DO NOTHING;