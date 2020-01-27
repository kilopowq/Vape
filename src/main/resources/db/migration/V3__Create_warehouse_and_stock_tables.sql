CREATE TABLE warehouse (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE stock (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGSERIAL NOT NULL,
    warehouse_id BIGSERIAL NOT NULL,
    quantity BIGSERIAL NOT NULL
);

ALTER TABLE warehouse
ADD CONSTRAINT warehouse_name_unique UNIQUE (name);

ALTER TABLE stock
  ADD CONSTRAINT stock_product_fk
FOREIGN KEY (product_id) REFERENCES product;

ALTER TABLE stock
  ADD CONSTRAINT stock_warehouse_fk
FOREIGN KEY (warehouse_id) REFERENCES warehouse;

ALTER TABLE stock
  ADD CONSTRAINT stock_positive_quantity CHECK (quantity >= 0);

ALTER TABLE stock
ADD CONSTRAINT stock_product_warehouse_unique UNIQUE (product_id, warehouse_id);