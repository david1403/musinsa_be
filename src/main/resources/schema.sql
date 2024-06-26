DROP TABLE IF EXISTS CATEGORY;
CREATE TABLE CATEGORY (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    category_name VARCHAR(255),
    PRIMARY KEY (category_id)
);

DROP TABLE IF EXISTS BRAND;
CREATE TABLE BRAND (
    brand_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    brand_name VARCHAR(255),
    PRIMARY KEY (brand_id)
);

DROP TABLE IF EXISTS PRODUCT;
CREATE TABLE PRODUCT (
    product_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    category_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    price BIGINT NOT NULL,
    PRIMARY KEY (product_id)
);