--liquibase formatted sql
--changeset niksakh:4

CREATE TABLE deals (
                       id UUID PRIMARY KEY,
                       lead_id UUID REFERENCES leads(id) ON DELETE SET NULL,
                       title VARCHAR(255) NOT NULL,
                       amount DECIMAL(15, 2) NOT NULL,
                       currency VARCHAR(3) DEFAULT 'USD',
                       stage VARCHAR(50) NOT NULL,
                       probability INTEGER,
                       expected_close_date DATE,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);