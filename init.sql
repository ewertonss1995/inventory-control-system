CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    registration_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP
);

INSERT INTO categories (category_name, registration_date, update_date)
VALUES
   ('MODA', NOW(), NULL),
   ('ELETRÔNICOS', NOW(), NULL),
   ('MOVEIS', NOW(), NULL);


CREATE TABLE products (
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