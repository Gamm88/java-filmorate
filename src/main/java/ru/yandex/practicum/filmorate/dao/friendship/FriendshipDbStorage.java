package ru.yandex.practicum.filmorate.dao.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addToFriends(Long userId, Long friendId) {
        final String sqlQuery = "INSERT INTO friendships (user_id, friend_id) values (?,?)";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFromFriends(Long userId, Long friendId) {
        final String sqlQuery = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
