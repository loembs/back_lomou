-- Catégories
-- Catégories
INSERT INTO category (id, name) VALUES 
(1, 'Boubous'),
(2, 'Robes'),
(3, 'Bijoux'),
(4, 'Accessoires');


-- Produits
-- Script d'insertion des produits avec gestion du stock
-- Inclut les nouveaux champs : stock, available, last_modified

-- D'abord, s'assurer que les colonnes existent
ALTER TABLE product ADD COLUMN IF NOT EXISTS stock INTEGER DEFAULT 0;
ALTER TABLE product ADD COLUMN IF NOT EXISTS available BOOLEAN DEFAULT TRUE;
ALTER TABLE product ADD COLUMN IF NOT EXISTS last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Insérer les produits avec les données complètes
INSERT INTO product (
    id, name, description, price, image_url, category_id, stock, available, last_modified
) VALUES
      (1, 'Boubou wax brodé main', 'Boubou traditionnel confectionné à la main au Sénégal, tissu wax premium, broderies artisanales.', 25000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/Toutes_mes_bubu_yccuc6.jpg', 1, 15, TRUE, CURRENT_TIMESTAMP),
      (2, 'Robe longue pagne africain', 'Robe longue en pagne africain, coupe élégante, motifs colorés, idéale pour toutes occasions.', 20000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/QUEL_TA_PHOTO_PR%C3%89F%C3%89R%C3%89E_sgdo8v.jpg', 2, 12, TRUE, CURRENT_TIMESTAMP),
      (3, 'Robe africaine', 'Robe artisanale, fabriquée à la main par des artisans sénégalais.', 15000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/Hi_babe_cbwten.jpg', 2, 8, TRUE, CURRENT_TIMESTAMP),
      (4, 'Boubou Royal', 'Robe brodée, fabriquée à la main par des artisans sénégalais.', 25000, 'https://res.cloudinary.com/dlna2kuo1/image/upload/v1753976305/bouvou_royale_ybgfxn.jpg', 1, 5, TRUE, CURRENT_TIMESTAMP);
-- Si les produits existent déjà, les mettre à jour avec les nouvelles données
-- (Alternative si tu veux mettre à jour au lieu d'insérer)
/*
UPDATE product SET
    stock = 15,
    available = TRUE,
    last_modified = CURRENT_TIMESTAMP
WHERE id = 1;

UPDATE product SET
    stock = 12,
    available = TRUE,
    last_modified = CURRENT_TIMESTAMP
WHERE id = 2;

UPDATE product SET
    stock = 8,
    available = TRUE,
    last_modified = CURRENT_TIMESTAMP
WHERE id = 3;
*/

-- Vérifier l'insertion
--SELECT id, name, price, stock, available, last_modified FROM product ORDER BY id;

-- Script de création de la table users pour l'authentification
-- Inclut tous les champs nécessaires pour JWT et OAuth

-- Créer la table users si elle n'existe pas
-- Script de création de la table users pour l'authentification
-- Inclut tous les champs nécessaires pour JWT et OAuth

-- Créer la table users si elle n'existe pas
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

-- Créer les index
CREATE INDEX IF NOT EXISTS idx_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_oauth ON users (oauth_provider, oauth_id);
CREATE INDEX IF NOT EXISTS idx_role ON users (role);
CREATE INDEX IF NOT EXISTS idx_enabled ON users (enabled);

-- Insérer un utilisateur admin par défaut (mot de passe: admin123)
INSERT INTO users (email, password, first_name, last_name, role, enabled) VALUES
    ('admin@loumo.com', '$2a$10$nOUIs2Gov7K89nX/Y5Xfbue7XGo9V1ZU9Rpp8DJOTOLZhNz/f9C6K', 'Admin', 'Loumo', 'ADMIN', TRUE)
    ON CONFLICT (email) DO NOTHING;

-- Insérer quelques utilisateurs clients de test
INSERT INTO users (email, password, first_name, last_name, role, phone, address, enabled) VALUES
                                                                                              ('client1@example.com', '$2a$10$nOUIs2Gov7K89nX/Y5Xfbue7XGo9V1ZU9Rpp8DJOTOLZhNz/f9C6K', 'Jean', 'Dupont', 'CLIENT', '+221 77 123 45 67', 'Dakar, Sénégal', TRUE),
                                                                                              ('client2@example.com', '$2a$10$nOUIs2Gov7K89nX/Y5Xfbue7XGo9V1ZU9Rpp8DJOTOLZhNz/f9C6K', 'Marie', 'Martin', 'CLIENT', '+221 78 987 65 43', 'Thiès, Sénégal', TRUE)
    ON CONFLICT (email) DO NOTHING;

-- Vérifier l'insertion
SELECT id, email, first_name, last_name, role, enabled, created_at FROM users ORDER BY id;



-- Script de création des tables pour la gestion des commandes
-- Tables: orders et order_items

-- Table des commandes
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
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Table des éléments de commande
CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           product_id BIGINT NOT NULL,
                                           quantity INTEGER NOT NULL,
                                           unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
    );

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_order_date ON orders(order_date);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product_id ON order_items(product_id);

-- Contraintes de validation
ALTER TABLE orders ADD CONSTRAINT chk_orders_status
    CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'));

ALTER TABLE orders ADD CONSTRAINT chk_orders_total_amount
    CHECK (total_amount >= 0);

ALTER TABLE order_items ADD CONSTRAINT chk_order_items_quantity
    CHECK (quantity > 0);

ALTER TABLE order_items ADD CONSTRAINT chk_order_items_unit_price
    CHECK (unit_price >= 0);

ALTER TABLE order_items ADD CONSTRAINT chk_order_items_total_price
    CHECK (total_price >= 0);

-- Commentaires pour la documentation
COMMENT ON TABLE orders IS 'Table des commandes clients';
COMMENT ON TABLE order_items IS 'Table des éléments de commande';
COMMENT ON COLUMN orders.status IS 'Statut de la commande: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED';
COMMENT ON COLUMN orders.order_number IS 'Numéro unique de commande généré automatiquement';