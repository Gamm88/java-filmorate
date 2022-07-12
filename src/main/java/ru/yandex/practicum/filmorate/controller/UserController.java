package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер пользователей
 * Методы - получить, создать, обновить
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private long id = 0; // уникальный ID пользователя
    private final Map<Long, User> users = new HashMap<>(); // хранение пользователей

    // получить всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    // создать пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        // проверяем на дублирование пользователей (по почте)
        if (emailCheck(user.getEmail())) {
            log.debug("Пользователь с электронной почтой уже зарегистрирован: {}", user.getEmail());
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        // имя может быть пустым — в таком случае будет использован логин;
        if (nameCheck(user.getName())) {
            user.setName(user.getLogin());
        }
        // создаём пользователя
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь: {}", user);
        return user;
    }

    // обновить пользователя
    @PutMapping
    public User put(@Valid @RequestBody User user) {
        // проверяем, что обновляемый пользователь существует
        if (!users.containsKey(user.getId())) {
            log.debug("Пользователь с таким ID не найден: {}", user.getId());
            throw new ValidationException("Пользователь с ID " + user.getId() + " не найден");
        }
        // если изменена электронная почта, проверяем на дублирование с другими пользователями
        if (users.get(user.getId()).getEmail() != user.getEmail()) {
            if (emailCheck(user.getEmail())) {
                log.debug("Пользователь с электронной почтой уже зарегистрирован: {}", user.getEmail());
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
        // имя может быть пустым — в таком случае будет использован логин;
        if (nameCheck(user.getName())) {
            user.setName(user.getLogin());
        }
        // обновляем пользователя
        users.put(user.getId(), user);
        log.debug("Обновлён пользователь: {}", user);
        return user;
    }

    // проверка указано имя или нет
    private boolean nameCheck(String name) {
        if (name == null || name.isBlank()) {
            return true;
        }
        return false;
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