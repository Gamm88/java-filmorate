package ru.yandex.practicum.filmorate.dao.like;

/**
 * Интерфейс для добавления и удаления лайков фильмам
 */
public interface LikeStorage {
    // добавить лайк фильму
    void addLikeToFilm(Long filmId, Long userId);

    // удалить лайк у фильма
    void deleteLikeFromFilm(Long filmId, Long userId);
}