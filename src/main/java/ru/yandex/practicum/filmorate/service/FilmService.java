package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.List;

/**
 * Сервисы для фильмов
 * поставить или удалить лайк фильму, получить топ фильмов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    // создать фильм
    public Film createFilm(Film film) {
        log.info("Создание фильма: {}", film);
        return filmStorage.createFilm(film);
    }

    // получить все фильмы
    public List<Film> getAllFilms() {
        log.info("Получение списка фильмов");
        return filmStorage.getAllFilms();
    }

    // получить фильм по ИД
    public Film getFilmById(Long filmId) {
        checkingForExistenceFilm(filmId);
        log.info("Получение фильма по Ид: {}", filmStorage.getFilmById(filmId));
        return filmStorage.getFilmById(filmId);
    }

    // обновление фильма
    public Film updateFilm(Film film) {
        checkingForExistenceFilm(film.getId());
        log.info("Обновление фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    // удалить все фильмы
    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    // удалить фильм по ИД
    public void deleteFilmById(Long filmId) {
        checkingForExistenceFilm(filmId);
        log.info("Удаление фильма по Ид: {}", filmId);
        filmStorage.deleteFilmById(filmId);
    }

    // добавить лайк фильму
    public void addLikeToFilm(Long filmId, Long userId) {
        checkingForExistenceFilm(filmId);
        checkingForExistenceUser(userId);
        log.info("Пользователь с ИД=" + userId + " ставит лайк у фильму с ИД=" + filmId);
        likeStorage.addLikeToFilm(filmId, userId);
    }

    // удалить лайк у фильма
    public void deleteLikeFromFilm(Long filmId, Long userId) {
        checkingForExistenceFilm(filmId);
        checkingForExistenceUser(userId);
        log.info("Пользователь с ИД=" + userId + " удаляет лайк у фильма с ИД=" + filmId);
        likeStorage.deleteLikeFromFilm(filmId, userId);
    }

    // получить топ фильмов по количеству лайков (количество фильмов ограничено числом count)
    public List<Film> getMostPopularFilms(Integer count) {
        if (count < 1) {
            throw new IncorrectParameterException("Список фильмов не может быть меньше 1, указан:" + count);
        }
        log.info("Получить топ " + count + " фильмов");
        return filmStorage.getMostPopularFilms(count);
    }

    // проверка на существование фильма, по ИД
    private void checkingForExistenceFilm(Long filmId) {
        if (filmId < 0 || filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Пользователь с ИД " + filmId + " не найден");
        }
    }

    // проверка на существование пользователя, по ИД
    private void checkingForExistenceUser(Long userId) {
        if (userId < 0 || userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }
    }
}
