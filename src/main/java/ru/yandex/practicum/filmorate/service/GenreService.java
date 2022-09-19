package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

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
