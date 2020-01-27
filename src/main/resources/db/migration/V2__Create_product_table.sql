CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE product
ADD CONSTRAINT product_name_unique UNIQUE (name);