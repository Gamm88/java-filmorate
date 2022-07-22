package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

/**
 * Класс для хранения фильмов
 * и реализация CRUD методов
 */
@Slf4j
@Component
@RestControllerAdvice
public class InMemoryFilmStorage implements FilmStorage {
    private long id = 0; // уникальный ID фильма
    private final HashMap<Long, Film> films = new HashMap<>(); // хранение фильмов

    // создать фильм
    @Override
    public Film createFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    // получить все фильмы
    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    // получить фильм по ИД
    @Override
    public Film getFilmById(Long filmId) {
        return films.get(filmId);
    }

    // обновление фильма
    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    // удалить все фильмы
    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    // удалить фильм по ИД
    @Override
    public void deleteFilmById(Long filmId) {
        films.remove(filmId);
    }
}