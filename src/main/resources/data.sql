-- Catégories
-- Catégories
INSERT INTO category (id, name) VALUES 
(1, 'Boubous'),
(2, 'Robes'),
(3, 'Bijoux'),
(4, 'Accessoires');


-- Produits
INSERT INTO product (id, name, description, price, image_url, category_id) VALUES
(1, 'Boubou wax brodé main', 'Boubou traditionnel confectionné à la main au Sénégal, tissu wax premium, broderies artisanales.', 25000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/Toutes_mes_bubu_yccuc6.jpg', 1),
(2, 'Robe longue pagne africain', 'Robe longue en pagne africain, coupe élégante, motifs colorés, idéale pour toutes occasions.', 20000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/QUEL_TA_PHOTO_PR%C3%89F%C3%89R%C3%89E_sgdo8v.jpg', 2),
(3, 'Robe africaine', 'Robe artisanal , fabriqué à la main par des artisans sénégalais.', 15000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1753278166/Hi_babe_cbwten.jpg', 2);
