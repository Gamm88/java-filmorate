--DROP TABLE IF EXISTS friendships, likes, film_genre, genres, films, mpa, users;

CREATE TABLE IF NOT EXISTS users (
    user_id    LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login      VARCHAR(50)  NOT NULL UNIQUE,
    user_name  VARCHAR(50),
    email      VARCHAR(100) NOT NULL UNIQUE,
    birthday   DATE
    CONSTRAINT check_users_login_not_empty    CHECK login NOT LIKE '% %' AND login != '',
    CONSTRAINT check_users_email_not_empty    CHECK email NOT LIKE '% %' AND email != '',
    CONSTRAINT check_users_birth_date_in_past CHECK birthday < CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS friendships (
    user_id     LONG NOT NULL,
    friend_id   LONG NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id)   REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id     LONG generated by default as identity primary key,
    mpa_name   VARCHAR NOT NULL UNIQUE,
    CONSTRAINT check_mpa_name_not_empty CHECK mpa_name != ''
);

CREATE TABLE IF NOT EXISTS films (
    film_id      LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name    VARCHAR NOT NULL,
    description  VARCHAR(200),
    release_date DATE,
    duration     INT,
    film_mpa     LONG REFERENCES mpa (mpa_id),
    CONSTRAINT   check_films_name_not_empty    CHECK film_name != '',
    CONSTRAINT   check_films_positive_duration CHECK duration > 0,
    CONSTRAINT   check_films_release_date_after_1895_12_28 CHECK release_date >= PARSEDATETIME('1895-12-28', 'yyyy-MM-dd')
);

CREATE TABLE IF NOT EXISTS likes (
    film_id     LONG NOT NULL,
    user_id     LONG NOT NULL,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id    LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name  VARCHAR NOT NULL UNIQUE,
    CONSTRAINT  check_genres_name_not_empty CHECK genre_name != ''
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id     LONG NOT NULL,
    genre_id    LONG NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id)  REFERENCES films  (film_id)  ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id) ON DELETE CASCADE
);