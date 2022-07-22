package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
/**
 * Интерфейс для хранения фильмов
 * и CRUD методов
 */
public interface FilmStorage {
    // создать фильм
    Film createFilm(Film film);

    // получить все фильмы
    Collection<Film> getAllFilms();

    // получить фильм по ИД
    Film getFilmById(Long filmId);

    // обновление фильма
    Film updateFilm(Film film);

    // удалить все фильмы
    void deleteAllFilms();

    // удалить фильм по ИД
    void deleteFilmById(Long filmId);
}
