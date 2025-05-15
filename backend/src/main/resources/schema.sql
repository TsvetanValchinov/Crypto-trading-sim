CREATE TABLE accounts(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance DECIMAL(20,8) NOT NULL CHECK (balance >= 0),
    --initial_balance DECIMAL(20,8) NOT NULL CHECK (initial_balance >= 0), TODO: do i need initial balance
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT UC_user_id UNIQUE (user_id)
);

CREATE  TABLE cryptocurrency_assets(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    account_id BIGINT NOT NULL,
    symbol VARCHAR(64) NOT NULL,
    quantity DECIMAL(20,8) NOT NULL CHECK (quantity >= 0),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_assets_account FOREIGN KEY (account_id)
        REFERENCES accounts(id) ON DELETE CASCADE,
    CONSTRAINT UC_account_symbol UNIQUE (account_id, symbol)
);

CREATE TABLE transactions(
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
    account_id BIGINT FOREIGN KEY REFERENCES accounts(id),
    symbol VARCHAR(64) NOT NULL,
    quantity DECIMAL(20,8) NOT NULL,
    price DECIMAL(20,8) NOT NULL,
    type VARCHAR(64) NOT NULL CHECK (type IN ('BUY', 'SELL')),
    timestamp DATETIME DEFAULT GETDATE(),
    profit_loss DECIMAL(20,8) NULL,
    CONSTRAINT FK_transaction_account FOREIGN KEY (account_id)
        REFERENCES accounts(id) ON DELETE CASCADE
);