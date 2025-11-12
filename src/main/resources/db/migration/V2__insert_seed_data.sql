-- Стили
INSERT INTO beer_styles (name, description) VALUES
    ('IPA', 'India Pale Ale: горькое, хмельное пиво') ON CONFLICT DO NOTHING;
INSERT INTO beer_styles (name, description) VALUES
    ('Lager', 'Легкое, освежающее пиво низового брожения') ON CONFLICT DO NOTHING;
INSERT INTO beer_styles (name, description) VALUES
    ('Stout', 'Темное, плотное пиво с нотами кофе и шоколада') ON CONFLICT DO NOTHING;

-- Пивоварни
INSERT INTO breweries (name, location, description) VALUES
    ('BrewDog', 'Scotland', 'Инновационная craft-пивоварня') ON CONFLICT DO NOTHING;
INSERT INTO breweries (name, location, description) VALUES
    ('Heineken', 'Netherlands', 'Крупный производитель лагеров') ON CONFLICT DO NOTHING;
INSERT INTO breweries (name, location, description) VALUES
    ('Guinness', 'Ireland', 'Известны стаутами') ON CONFLICT DO NOTHING;

-- Пиво
INSERT INTO beers (name, description, abv, ibu, brewery_id, style_id) VALUES
    ('Punk IPA', 'Классический IPA с тропическими нотами', 5.6, 45, (SELECT id FROM breweries WHERE name = 'BrewDog'), (SELECT id FROM beer_styles WHERE name = 'IPA')) ON CONFLICT DO NOTHING;
INSERT INTO beers (name, description, abv, ibu, brewery_id, style_id) VALUES
    ('Heineken Lager', 'Стандартный лагер', 5.0, 23, (SELECT id FROM breweries WHERE name = 'Heineken'), (SELECT id FROM beer_styles WHERE name = 'Lager')) ON CONFLICT DO NOTHING;
INSERT INTO beers (name, description, abv, ibu, brewery_id, style_id) VALUES
    ('Guinness Draught', 'Кремовый стаут', 4.2, 45, (SELECT id FROM breweries WHERE name = 'Guinness'), (SELECT id FROM beer_styles WHERE name = 'Stout')) ON CONFLICT DO NOTHING;

-- Ингредиенты (для первого пива, как пример)
INSERT INTO beer_ingredients (beer_id, ingredient) VALUES
    ((SELECT id FROM beers WHERE name = 'Punk IPA'), 'Хмель') ON CONFLICT DO NOTHING;
INSERT INTO beer_ingredients (beer_id, ingredient) VALUES
    ((SELECT id FROM beers WHERE name = 'Punk IPA'), 'Солод') ON CONFLICT DO NOTHING;
INSERT INTO beer_ingredients (beer_id, ingredient) VALUES
    ((SELECT id FROM beers WHERE name = 'Punk IPA'), 'Дрожжи') ON CONFLICT DO NOTHING;

-- Пользователи
INSERT INTO users (username, email) VALUES
    ('user1', 'user1@example.com') ON CONFLICT DO NOTHING;
INSERT INTO users (username, email) VALUES
    ('user2', 'user2@example.com') ON CONFLICT DO NOTHING;

-- Отзывы
INSERT INTO reviews (user_id, beer_id, rating, comment) VALUES
    ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM beers WHERE name = 'Punk IPA'), 4.5, 'Отличный хмельный вкус!') ON CONFLICT DO NOTHING;
INSERT INTO reviews (user_id, beer_id, rating, comment) VALUES
    ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM beers WHERE name = 'Punk IPA'), 4.0, 'Хорошо, но слишком горькое') ON CONFLICT DO NOTHING;
INSERT INTO reviews (user_id, beer_id, rating, comment) VALUES
    ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM beers WHERE name = 'Heineken Lager'), 3.5, 'Стандартно, ничего особенного') ON CONFLICT DO NOTHING;