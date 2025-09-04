CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(50) NOT NULL UNIQUE,
    product_description VARCHAR(100) NOT NULL,
    unit_price DECIMAL(8, 2) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(8, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    remove_date TIMESTAMP NULL.
    update_date TIMESTAMP NULL
);