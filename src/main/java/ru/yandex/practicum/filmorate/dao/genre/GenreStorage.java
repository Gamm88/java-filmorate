package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

/**
 * Интерфейс для хранения жанров фильмов
 * и методов получения жанров
 */
public interface GenreStorage {
    // получить все жанры
    List<Genre> getAllGenre();

    // получить жанр по ИД
    Genre getGenreById(Long genreId);
}
