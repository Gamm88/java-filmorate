package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/**
 * Контроллер фильмов
 * Методы - получить, создать, обновить
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 0; // уникальный ID фильма
    private Map<Integer, Film> films = new HashMap<>(); // хранение фильмов

    // получить всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    // создать фильм
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);
        return film;
    }

    // обновить фильма
    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        // проверяем, что обновляемый фильм существует
        if(!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с ID " + film.getId() + " не найден");
        }
        // обновляем фильм
        films.put(film.getId(), film);
        log.debug("Обновлён фильм: {}", film);
        return film;
    }
}