package ru.yandex.practicum.filmorate.dao.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        final String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES ( ?, ? )";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(Long filmId, Long userId) {
        final String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

}
