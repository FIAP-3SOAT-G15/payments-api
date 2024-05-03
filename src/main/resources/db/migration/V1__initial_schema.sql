CREATE TABLE IF NOT EXISTS product
(
    product_number SERIAL NOT NULL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_category VARCHAR(255) NOT NULL,
    product_price NUMERIC(15,2) NOT NULL,
    product_description VARCHAR(255),
    product_min_sub_item INTEGER NOT NULL,
    product_max_sub_item INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS component (
    component_number SERIAL NOT NULL PRIMARY KEY,
    component_name VARCHAR(2000) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_component (
    product_component_product_number SERIAL NOT NULL,
    product_component_component_number SERIAL NOT NULL,
    CONSTRAINT pk_product_component PRIMARY KEY(product_component_product_number, product_component_component_number),
    CONSTRAINT fk_product_component_product_number FOREIGN KEY(product_component_product_number) REFERENCES product(product_number) ON DELETE CASCADE,
    CONSTRAINT fk_product_component_component_number FOREIGN KEY(product_component_component_number) REFERENCES component(component_number) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS stock
(
    stock_component_number SERIAL NOT NULL PRIMARY KEY,
    stock_quantity BIGINT NOT NULL,
    CONSTRAINT fk_stock_component_number FOREIGN KEY(stock_component_number) REFERENCES component(component_number) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS product_sub_item
(
    product_sub_item_id SERIAL PRIMARY KEY,
    product_sub_item_product_id_parent INTEGER NOT NULL,
    product_sub_item_product_id_sub INTEGER NOT NULL,
    CONSTRAINT fk_product_sub_item_product_id_parent FOREIGN KEY(product_sub_item_product_id_parent) REFERENCES product(product_number),
    CONSTRAINT fk_product_sub_item_product_id_sub FOREIGN KEY(product_sub_item_product_id_sub) REFERENCES product(product_number)
);
