package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для хранения пользователей
 * и реализация CRUD методов
 */
@Slf4j
@Component
@RestControllerAdvice
public class InMemoryUserStorage implements UserStorage {
    private long id = 0; // уникальный ID пользователя
    private final Map<Long, User> users = new HashMap<>(); // хранение пользователей

    // создать пользователя
    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    // получить всех пользователей
    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    // получить пользователя по ИД
    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    // обновление пользователя
    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    // удалить всех пользователей
    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    // удалить пользователя по ИД
    @Override
    public void deleteUserById(Long userId) {
        // проверяем, что удаляемый фильм существует
        final User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ИД=" + userId + " не найден");
        }
        // удаляем фильм
        users.remove(userId);
        log.debug("Удаление пользователя по Ид: {}", userId);
    }
}