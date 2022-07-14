package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Сервисы для фильмов
 * поставить или удалить лайк фильму, получить топ фильмов
 */
@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    // добавить лайк фильму
    public boolean addLikeToFilm(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) != null && userStorage.getUserById(userId) != null) {
            filmStorage.getFilmById(filmId).getLikes().add(userId);
        } else throw new ValidationException("Неверный ID фильма и/или Пользователя");
        log.debug("Пользователь " + userId + " ставит лайк у фильму" + filmId);
        return filmStorage.getFilmById(filmId).getLikes().contains(userId);
    }

    // удалить лайк у фильма
    public boolean deleteLikeForFilm(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) != null && userStorage.getUserById(userId) != null) {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        } else throw new ValidationException("Неверный ID фильма и/или Пользователя");
        log.debug("Пользователь " + userId + " удаляет лайк у фильма" + filmId);
        return !filmStorage.getFilmById(filmId).getLikes().contains(userId);
    }

    // получить топ фильмов по количеству лайков (количество фильмов ограничено числом count)
    public List<Film> mostPopularFilms(Integer count) {
        if (count < 1) {
            throw new IncorrectParameterException("Список фильмов не может быть меньше 1, указан:" + count);
        }
        List<Film> mostPopularFilms = new ArrayList<>(filmStorage.getAllFilms());
        log.debug("Получить топ " + count + "фильмов");
        return mostPopularFilms.stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
