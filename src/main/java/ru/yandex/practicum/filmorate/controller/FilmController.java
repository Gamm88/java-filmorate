package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * Контроллер фильмов
 * содержит эндпоинты
 */
@RestController
@RestControllerAdvice
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // создать фильм
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    // получить все фильмы
    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.getAllFilms();
    }

    // получить фильм по ИД
    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        return filmService.getFilmById(filmId);
    }

    // обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    // удалить все фильмы
    @DeleteMapping
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
    }

    // удалить фильм по ИД
    @DeleteMapping("/{filmId}")
    public void deleteFilmById(@PathVariable("filmId") Long filmId) {
        filmService.deleteFilmById(filmId);
    }

    // поставить лайк фильму
    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLikeToFilm(filmId, userId);
    }

    // удалить лайк у фильма
    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLikeForFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.deleteLikeFromFilm(filmId, userId);
    }

    // получить самые популярные фильмы по количеству лайков, если значение параметра count не задано, вернуть топ 10
    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getMostPopularFilms(count);
    }
}