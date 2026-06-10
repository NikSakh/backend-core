--liquibase formatted sql
--changeset niksakh:1

CREATE TABLE companies (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           industry VARCHAR(100)
);