package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        // проверяем на дублирование пользователей (по почте)
        if (emailCheck(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        // имя может быть пустым — в таком случае будет использован логин;
        nameCheck(user);
        // создаём пользователя
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Добавление пользователя: {}", user);
        return user;
    }

    // получить всех пользователей
    @Override
    public Collection<User> getAllUsers() {
        log.debug("Получение всех пользователей");
        return users.values();
    }

    // получить пользователя по ИД
    @Override
    public User getUserById(Long userId) {
        final User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ИД=" + userId + " не найден");
        }
        log.debug("Получение пользователя по ИД: {}", userId);
        return user;
    }

    // обновление пользователя
    @Override
    public User updateUser(User user) {
        // проверяем, что обновляемый пользователь существует
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }
        // если изменена электронная почта, проверяем на дублирование с другими пользователями
        if (!users.get(user.getId()).getEmail().equals(user.getEmail())) {
            if (emailCheck(user.getEmail())) {
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
        // имя может быть пустым — в таком случае будет использован логин;
        nameCheck(user);
        // обновляем пользователя
        users.put(user.getId(), user);
        log.debug("Обновлён пользователь: {}", user);
        return user;
    }

    // удалить всех пользователей
    @Override
    public boolean deleteAllUsers() {
        users.clear();
        log.debug("Удаление всех пользователей");
        return true;
    }

    // удалить пользователя по ИД
    @Override
    public boolean deleteUserById(Long userId) {
        // проверяем, что удаляемый фильм существует
        final User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ИД=" + userId + " не найден");
        }
        // удаляем фильм
        users.remove(userId);
        log.debug("Удаление пользователя по Ид: {}", userId);
        return true;
    }

    // проверка указано имя или нет
    private void nameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    // проверка на дублирование пользователей (по почте)
    private boolean emailCheck(String email) {
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (entry.getValue().getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}