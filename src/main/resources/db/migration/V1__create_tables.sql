-- Таблица стилей пива
CREATE TABLE IF NOT EXISTS beer_styles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
    );

-- Таблица пивоварен
CREATE TABLE IF NOT EXISTS breweries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(200),
    description TEXT
    );

-- Таблица пива
CREATE TABLE IF NOT EXISTS beers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    abv DECIMAL(5,2) NOT NULL,
    ibu INT,
    brewery_id INT NOT NULL,
    style_id INT NOT NULL,
    FOREIGN KEY (brewery_id) REFERENCES breweries(id) ON DELETE CASCADE,
    FOREIGN KEY (style_id) REFERENCES beer_styles(id) ON DELETE CASCADE
    );

-- Таблица ингредиентов (для @ElementCollection в Beer)
CREATE TABLE IF NOT EXISTS beer_ingredients (
    beer_id BIGINT NOT NULL,
    ingredient VARCHAR(255),
    FOREIGN KEY (beer_id) REFERENCES beers(id) ON DELETE CASCADE
    );

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE
    );

-- Таблица отзывов
CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    beer_id BIGINT NOT NULL,
    rating DECIMAL(2,1) NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (beer_id) REFERENCES beers(id) ON DELETE CASCADE
    );

-- Индексы
CREATE INDEX IF NOT EXISTS idx_beers_name ON beers(name);
CREATE INDEX IF NOT EXISTS idx_reviews_beer_id ON reviews(beer_id);