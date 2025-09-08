CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    registration_date TIMESTAMP NOT NULL DEFAULT NULL,
    update_date TIMESTAMP DEFAULT NULL
);

ALTER TABLE products
DROP COLUMN category;
ADD COLUMN category_id INTEGER NOT NULL,
ADD FOREIGN KEY (category_id) REFERENCES categories(category_id),
RENAME COLUMN remove_date TO update_date DEFAULT NULL;