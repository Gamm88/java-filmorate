package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;
/**
 * Контроллер фильмов
 * содержит эндпоинты
 */
@RestController
@RestControllerAdvice
@RequestMapping("/films")
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    // создать фильм
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    // получить все фильмы
    @GetMapping
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.getAllFilms();
    }

    // получить фильм по ИД
    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        return inMemoryFilmStorage.getFilmById(filmId);
    }

    // обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    // удалить все фильмы
    @DeleteMapping
    public boolean deleteAllFilms() {
        return inMemoryFilmStorage.deleteAllFilms();
    }

    // удалить фильм по ИД
    @DeleteMapping("/{filmId}")
    public boolean deleteFilmById(@PathVariable("filmId") long filmId) {
        return inMemoryFilmStorage.deleteFilmById(filmId);
    }

    // поставить лайк фильму
    @PutMapping("/{filmId}/like/{userId}")
    public boolean addLikeToFilm(@PathVariable long filmId, @PathVariable long userId) {
        return filmService.addLikeToFilm(filmId, userId);
    }

    // удалить лайк у фильма
    @DeleteMapping ("/{filmId}/like/{userId}")
    public boolean deleteLikeForFilm(@PathVariable long filmId, @PathVariable long userId) {
        return filmService.deleteLikeForFilm(filmId, userId);
    }

    // получить самые популярные фильмы по количеству лайков, если значение параметра count не задано, вернуть топ 10
    @GetMapping("/popular")
    public Collection<Film> mostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.mostPopularFilms(count);
    }
}