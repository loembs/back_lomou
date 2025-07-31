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