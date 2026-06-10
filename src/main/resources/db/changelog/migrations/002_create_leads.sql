--liquibase formatted sql
--changeset niksakh:2

CREATE TABLE leads (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone VARCHAR(50),
                       company VARCHAR(255),
                       status VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       version BIGINT DEFAULT 0 NOT NULL,
                       company_id UUID REFERENCES companies(id)
);