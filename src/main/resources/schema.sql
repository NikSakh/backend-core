CREATE TABLE IF NOT EXISTS companies (
                                         id UUID PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
    industry VARCHAR(100)
    );

ALTER TABLE leads ADD COLUMN IF NOT EXISTS company_id UUID;
ALTER TABLE leads ADD CONSTRAINT IF NOT EXISTS fk_leads_company FOREIGN KEY (company_id) REFERENCES companies(id);

CREATE TABLE IF NOT EXISTS leads (
                                     id UUID PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    company VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    source VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
                             created_by UUID,
                             assigned_to UUID,
                             version BIGINT DEFAULT 0 NOT NULL,
                             company_id UUID REFERENCES companies(id)
    );

ALTER TABLE leads ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0 NOT NULL;

CREATE INDEX IF NOT EXISTS idx_leads_email ON leads(email);
CREATE INDEX IF NOT EXISTS idx_leads_status ON leads(status);
CREATE INDEX IF NOT EXISTS idx_leads_company_id ON leads(company_id);

CREATE TABLE IF NOT EXISTS contacts (
                                        id UUID PRIMARY KEY,
                                        lead_id UUID REFERENCES leads(id) ON DELETE CASCADE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    position VARCHAR(100),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
                                                                              );

CREATE INDEX IF NOT EXISTS idx_contacts_lead_id ON contacts(lead_id);

CREATE TABLE IF NOT EXISTS deals (
                                     id UUID PRIMARY KEY,
                                     lead_id UUID REFERENCES leads(id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    stage VARCHAR(50) NOT NULL,
    probability INTEGER CHECK (probability BETWEEN 0 AND 100),
    expected_close_date DATE,
    actual_close_date DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
                                                                           assigned_to UUID
                                                                           );

CREATE INDEX IF NOT EXISTS idx_deals_lead_id ON deals(lead_id);
CREATE INDEX IF NOT EXISTS idx_deals_stage ON deals(stage);

CREATE TABLE IF NOT EXISTS products (
                                        id UUID PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    sku VARCHAR(100) UNIQUE NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
    );