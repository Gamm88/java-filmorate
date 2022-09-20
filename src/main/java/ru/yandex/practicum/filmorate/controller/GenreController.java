package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

/**
 * Контроллер жанров фильмов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenre();
    }

    @GetMapping("/{genreId}")
    public Genre getGenre(@PathVariable("genreId") Long genreId) {
        return genreService.getGenreById(genreId);
    }
}
