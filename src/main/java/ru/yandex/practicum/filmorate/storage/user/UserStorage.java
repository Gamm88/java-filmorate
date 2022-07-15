package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс для хранения пользователей
 * и CRUD методов
 */
public interface UserStorage {
    // создать пользователя
    User createUser(User user);

    // получить всех пользователей
    Collection<User> getAllUsers();

    // получить пользователя по ИД
    User getUserById(Long userId);

    // обновление пользователя
    User updateUser(User user);

    // удалить всех пользователей
    void deleteAllUsers();

    // удалить пользователя по ИД
    void deleteUserById(Long userId);
}
