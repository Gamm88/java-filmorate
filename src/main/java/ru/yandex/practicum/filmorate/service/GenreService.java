package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public List<Genre> getAllGenre() {
        log.info("Получение списка genre");
        return genreDbStorage.getAllGenre();
    }

    public Genre getGenreById(Long genreId) {
        checkingForExistenceGenre(genreId);
        log.info("Получение genre по Ид: {}", genreId);
        return genreDbStorage.getGenreById(genreId);
    }

    private void checkingForExistenceGenre(Long genreId) {
        if (genreId < 0 || genreDbStorage.getGenreById(genreId) == null) {
            throw new NotFoundException("Жанр с ИД " + genreId + " не найден");
        }
    }
}
