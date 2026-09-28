-- =========================================================================
-- FALLBACK INITIALIZATION (Ensures users table exists if V1/V2 skipped)
-- =========================================================================
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

-- Safely add role column if it wasn't added by V2
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20);

-- =========================================================================
-- E-COMMERCE CORE TABLES (V3)
-- =========================================================================

-- 1. Products Table
CREATE TABLE IF NOT EXISTS products (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    price NUMERIC(19,2) NOT NULL,
    stock INTEGER NOT NULL
    );

-- 2. Carts Table
CREATE TABLE IF NOT EXISTS carts (
                                     id BIGSERIAL PRIMARY KEY,
                                     user_id BIGINT NOT NULL UNIQUE,
                                     total NUMERIC(19,2) NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

-- 3. Cart Products Table (Join Table)
CREATE TABLE IF NOT EXISTS cart_products (
                                             cart_id BIGINT NOT NULL,
                                             product_id BIGINT NOT NULL,
                                             PRIMARY KEY (cart_id, product_id),
    CONSTRAINT fk_cart_products_cart FOREIGN KEY (cart_id) REFERENCES carts(id),
    CONSTRAINT fk_cart_products_product FOREIGN KEY (product_id) REFERENCES products(id)
    );

-- 4. Purchases Table
CREATE TABLE IF NOT EXISTS purchases (
                                         id BIGSERIAL PRIMARY KEY,
                                         user_id BIGINT NOT NULL,
                                         total NUMERIC(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_purchases_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

-- 5. Purchase Products Table (Join Table)
CREATE TABLE IF NOT EXISTS purchase_products (
                                                 purchase_id BIGINT NOT NULL,
                                                 product_id BIGINT NOT NULL,
                                                 PRIMARY KEY (purchase_id, product_id),
    CONSTRAINT fk_purchase_products_purchase FOREIGN KEY (purchase_id) REFERENCES purchases(id),
    CONSTRAINT fk_purchase_products_product FOREIGN KEY (product_id) REFERENCES products(id)
    );

-- 6. Payments Table
CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        purchase_id BIGINT NOT NULL UNIQUE,
                                        amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_payments_purchase FOREIGN KEY (purchase_id) REFERENCES purchases(id)
    );
