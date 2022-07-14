package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        log.debug("Создание фильма: {}", film);
        return film;
    }

    // получить все фильмы
    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Получение списка фильмов");
        return films.values();
    }

    // получить фильм по ИД
    @Override
    public Film getFilmById(Long filmId) {
        final Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с ИД=" + filmId + " не найден");
        }
        log.debug("Получение фильма по Ид: {}", filmId);
        return film;
    }

    // обновление фильма
    @Override
    public Film updateFilm(Film film) {
        // проверяем, что обновляемый фильм существует
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден");
        }
        // обновляем фильм
        films.put(film.getId(), film);
        return film;
    }

    // удалить все фильмы
    @Override
    public boolean deleteAllFilms() {
        films.clear();
        return true;
    }

    // удалить фильм по ИД
    @Override
    public boolean deleteFilmById(Long filmId) {
        // проверяем, что удаляемый фильм существует
        final Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с ИД=" + filmId + " не найден");
        }
        // удаляем фильм
        films.remove(filmId);
        log.debug("Удаление фильма по Ид: {}", filmId);
        return true;
    }
}