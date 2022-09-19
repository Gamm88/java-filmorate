package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/**
 * Интерфейс для хранения пользователей
 * и CRUD методов
 */
public interface UserStorage {
    // создать пользователя
    User createUser(User user);

    // получить всех пользователей
    List<User> getAllUsers();

    // получить пользователя по ИД
    User getUserById(Long userId);

    // обновление пользователя
    User updateUser(User user);

    // удалить всех пользователей
    void deleteAllUsers();

    // удалить пользователя по ИД
    void deleteUserById(Long userId);

    void addToFriends(Long userId, Long friendId);

    void removeFromFriends(Long userId, Long friendId);

    List<User> getUserFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);
}
