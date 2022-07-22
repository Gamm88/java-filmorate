package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисы для фильмов
 * поставить или удалить лайк фильму, получить топ фильмов
 */
@Slf4j
@Service
@RestControllerAdvice
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    // создать фильм
    public Film createFilm(Film film) {
        log.debug("Создание фильма: {}", film);
        return filmStorage.createFilm(film);
    }

    // получить все фильмы
    public Collection<Film> getAllFilms() {
        log.debug("Получение списка фильмов");
        return filmStorage.getAllFilms();
    }

    // получить фильм по ИД
    public Film getFilmById(Long filmId) {
        if (checkingForExistenceFilm(filmId)) {
            throw new NotFoundException("Фильм с ИД=" + filmId + " не найден");
        }
        log.debug("Получение фильма по Ид: {}", filmId);
        return filmStorage.getFilmById(filmId);
    }

    // обновление фильма
    public Film updateFilm(Film film) {
        if (checkingForExistenceFilm(film.getId())) {
            throw new NotFoundException("Фильм с ИД=" + film.getId() + " не найден");
        }
        log.debug("Обновление фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    // удалить все фильмы
    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    // удалить фильм по ИД
    public void deleteFilmById(Long filmId) {
        if (checkingForExistenceFilm(filmId)) {
            throw new NotFoundException("Фильм с ИД=" + filmId + " не найден");
        }
        log.debug("Удаление фильма по Ид: {}", filmId);
        filmStorage.deleteFilmById(filmId);
    }

    // добавить лайк фильму
    public Film addLikeToFilm(Long filmId, Long userId) {
        if (checkingForExistenceFilm(filmId) || checkingForExistenceUser(userId)) {
            throw new NotFoundException("Фильм и/или пользователь не найдены, проверьте ИД");
        }
        log.debug("Пользователь с ИД=" + userId + " ставит лайк у фильму с ИД=" + filmId);
        filmStorage.getFilmById(filmId).getLikes().add(userId);
        return filmStorage.getFilmById(filmId);
    }

    // удалить лайк у фильма
    public Film deleteLikeFromFilm(Long filmId, Long userId) {
        if (checkingForExistenceFilm(filmId) || checkingForExistenceUser(userId)) {
            throw new NotFoundException("Фильм и/или пользователь не найдены, проверьте ИД");
        }
        log.debug("Пользователь с ИД=" + userId + " удаляет лайк у фильма с ИД=" + filmId);
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
        return filmStorage.getFilmById(filmId);
    }

    // получить топ фильмов по количеству лайков (количество фильмов ограничено числом count)
    public List<Film> getMostPopularFilms(Integer count) {
        if (count < 1) {
            throw new IncorrectParameterException("Список фильмов не может быть меньше 1, указан:" + count);
        }
        List<Film> mostPopularFilms = new ArrayList<>(filmStorage.getAllFilms());
        log.debug("Получить топ " + count + " фильмов");
        return mostPopularFilms.stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    // проверка на существование фильма, по ИД
    private boolean checkingForExistenceFilm(Long filmId) {
        return filmStorage.getFilmById(filmId) == null;
    }

    // проверка на существование пользователя, по ИД
    private boolean checkingForExistenceUser(Long userId) {
        return userStorage.getUserById(userId) == null;
    }
}
