-- Script d'initialisation pour la production
-- Ce script doit être exécuté manuellement une seule fois sur la base de données PostgreSQL

-- Créer les tables si elles n'existent pas
CREATE TABLE IF NOT EXISTS category (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS product (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(500),
    category_id BIGINT,
    stock INTEGER DEFAULT 0,
    available BOOLEAN DEFAULT TRUE,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
    );

CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(10) DEFAULT 'CLIENT' CHECK (role IN ('CLIENT', 'ADMIN')),
    phone VARCHAR(20),
    address TEXT,
    avatar VARCHAR(500),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    oauth_provider VARCHAR(50),
    oauth_id VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      order_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) NOT NULL,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    shipped_date TIMESTAMP NULL,
    delivered_date TIMESTAMP NULL,
    shipping_address VARCHAR(500) NULL,
    phone_number VARCHAR(20) NULL,
    customer_name VARCHAR(100) NULL,
    notes VARCHAR(500) NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           product_id BIGINT NOT NULL,
                                           quantity INTEGER NOT NULL,
                                           unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
    );

-- Créer les index
CREATE INDEX IF NOT EXISTS idx_product_category ON product(category_id);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_oauth ON users(oauth_provider, oauth_id);
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);

-- Insérer les données de base (seulement si elles n'existent pas)
INSERT INTO category (id, name) VALUES
                                    (1, 'Boubous'),
                                    (2, 'Robes'),
                                    (3, 'Bijoux'),
                                    (4, 'Accessoires')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO product (id, name, description, price, image_url, category_id, stock, available) VALUES
                                                                                                 (1, 'Boubou wax brodé main', 'Boubou traditionnel confectionné à la main au Sénégal, tissu wax premium, broderies artisanales.', 25000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/Toutes_mes_bubu_yccuc6.jpg', 1, 15, TRUE),
                                                                                                 (2, 'Robe longue pagne africain', 'Robe longue en pagne africain, coupe élégante, motifs colorés, idéale pour toutes occasions.', 20000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/QUEL_TA_PHOTO_PR%C3%89F%C3%89R%C3%89E_sgdo8v.jpg', 2, 12, TRUE),
                                                                                                 (3, 'Robe africaine', 'Robe artisanale, fabriquée à la main par des artisans sénégalais.', 15000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/Hi_babe_cbwten.jpg', 2, 8, TRUE),
                                                                                                 (4, 'Boubou Royal', 'Robe brodée, fabriquée à la main par des artisans sénégalais.', 25000, 'https://res.cloudinary.com/dlna2kuo1/image/upload/v1753976305/bouvou_royale_ybgfxn.jpg', 1, 5, TRUE)
    ON CONFLICT (id) DO NOTHING;

-- Insérer un utilisateur admin par défaut (mot de passe: admin123)
INSERT INTO users (email, password, first_name, last_name, role, enabled) VALUES
    ('admin@loumo.com', '$2a$10$nOUIs2Gov7K89nX/Y5Xfbue7XGo9V1ZU9Rpp8DJOTOLZhNz/f9C6K', 'Admin', 'Loumo', 'ADMIN', TRUE)
    ON CONFLICT (email) DO NOTHING;
