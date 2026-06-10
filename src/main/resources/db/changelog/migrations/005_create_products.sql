--liquibase formatted sql
--changeset niksakh:5

CREATE TABLE products (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          sku VARCHAR(100) UNIQUE NOT NULL,
                          price DECIMAL(19, 2) NOT NULL,
                          active BOOLEAN NOT NULL DEFAULT TRUE
);