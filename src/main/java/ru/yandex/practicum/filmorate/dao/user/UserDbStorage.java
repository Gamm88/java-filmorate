package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.sql.*;

@Repository
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        final String sqlQuery = "INSERT INTO users (login, user_name, email, birthday) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());

        return getUserById(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        final String sqlQuery = "SELECT user_id, login, user_name, email, birthday FROM users";

        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    @Override
    public User getUserById(Long userId) {
        final String sqlQuery = "SELECT user_id, login, user_name, email, birthday FROM users WHERE user_id = ?";

        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);

        return users.get(0);
    }

    @Override
    public User updateUser(User user) {
        final String sqlQuery = "UPDATE users SET login = ?, user_name = ?, email = ?, birthday = ? WHERE user_id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return getUserById(user.getId());
    }

    @Override
    public void deleteAllUsers() {
        final String sqlQuery = "DELETE users";

        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public void deleteUserById(Long userId) {
        final String sqlQuery = "DELETE FROM users WHERE user_id = ?";

        jdbcTemplate.update(sqlQuery, userId);
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

    @Override
    public List<User> getUserFriends(Long userId) {
        final String sqlQuery = "" +
                "SELECT * FROM users WHERE user_id in " +
                "(SELECT friend_id FROM friendships WHERE user_id = ?)";

        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        final String sqlQuery = "" +
                "SELECT * FROM users WHERE USER_ID IN (" +
                "SELECT friend_id FROM friendships WHERE user_id = ?" +
                "INTERSECT " +
                "SELECT friend_id FROM friendships WHERE user_id = ?)";

        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId, otherId);
    }

    static User makeUser(ResultSet resultSet, long rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("user_name"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}