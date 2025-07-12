-- Catégories
INSERT INTO category (id, name) VALUES 
('entrees', 'Entrées'),
('plats', 'Plats Principaux'),
('desserts', 'Desserts');

-- Plats
INSERT INTO plate (id, name, description, price, image_url, model3d_url, model_usdz_url, has_ar, difficulty, category_id) VALUES
(1, 'Salade de Quinoa', 'Quinoa bio, légumes grillés', 6000, 'https://res.cloudinary.com/dprbhsvxl/image/upload/v1751995446/plat-espagnol-paella-1024x743_zwos4i.jpg', 'https://enfgjsucpmsmrqxencxz.supabase.co/storage/v1/object/public/menu//Seafood_Feast_Paella_0708172133_texture.glb', 'https://enfgjsucpmsmrqxencxz.supabase.co/storage/v1/object/public/menu//Seafood_Feast_Paella_0708171431_texture.usdz', true, 'Végétarien', 'entrees'),
(2, 'Tartare de Saumon', 'Saumon frais, avocat, citron vert', 18000, 'https://res.cloudinary.com/.../plat2.jpg', '/models/plat2.glb', '/models/plat2.usdz', true, 'Frais', 'entrees'),
(3, 'Yassa de Poulet', 'Poulet mariné aux oignons', 2200, 'https://res.cloudinary.com/.../yassa.jpg', '/models/yassa.glb', '/models/yassa.usdz', true, 'Spécialité Africaine', 'plats'),
(4, 'Côte de Bœuf', 'Côte de bœuf 400g, sauce échalotes', 9000, 'https://res.cloudinary.com/.../boeuf.jpg', '/models/boeuf.glb', '/models/boeuf.usdz', true, 'Signature', 'plats'),
(5, 'Curry de Légumes', 'Légumes de saison, lait de coco', 8000, 'https://res.cloudinary.com/.../curry.jpg', '/models/curry.glb', '/models/curry.usdz', true, 'Végétalien', 'plats'),
(6, 'Tarte Tatin', 'Pommes caramélisées, glace vanille', 7500, 'https://res.cloudinary.com/.../tarte.jpg', '/models/tarte.glb', '/models/tarte.usdz', true, 'Fait Maison', 'desserts');

-- Correction des données existantes avec les bons caractères accentués
UPDATE category SET name = 'Entrées' WHERE id = 'entrees';
UPDATE category SET name = 'Plats Principaux' WHERE id = 'plats';
UPDATE category SET name = 'Desserts' WHERE id = 'desserts';

UPDATE plate SET 
  name = 'Salade de Quinoa',
  description = 'Quinoa bio, légumes grillés',
  difficulty = 'Végétarien'
WHERE id = 1;

UPDATE plate SET 
  name = 'Tartare de Saumon',
  description = 'Saumon frais, avocat, citron vert',
  difficulty = 'Frais'
WHERE id = 2;

UPDATE plate SET 
  name = 'Yassa de Poulet',
  description = 'Poulet mariné aux oignons',
  difficulty = 'Spécialité Africaine'
WHERE id = 3;

UPDATE plate SET 
  name = 'Côte de Bœuf',
  description = 'Côte de bœuf 400g, sauce échalotes',
  difficulty = 'Signature'
WHERE id = 4;

UPDATE plate SET 
  name = 'Curry de Légumes',
  description = 'Légumes de saison, lait de coco',
  difficulty = 'Végétalien'
WHERE id = 5;

UPDATE plate SET 
  name = 'Tarte Tatin',
  description = 'Pommes caramélisées, glace vanille',
  difficulty = 'Fait Maison'
WHERE id = 6;
