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

    private int id = 0; // уникальный ID пользователя
    private Map<Integer, User> users = new HashMap<>(); // хранение пользователей

    // получить всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    // создать пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        // проверяем на дублирование пользователей (по почте)
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if(entry.getValue().getEmail().equals(user.getEmail())) {
                log.debug("Пользователь с электронной почтой уже зарегистрирован: {}", user.getEmail());
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
        // имя может быть пустым — в таком случае будет использован логин;
        if(user.getName() == null || user.getName().isEmpty() ) {
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
        if(!users.containsKey(user.getId())) {
            log.debug("Пользователь с таким ID не найден: {}", user.getId());
            throw new ValidationException("Пользователь с ID " + user.getId() + " не найден");
        }
        // имя может быть пустым — в таком случае будет использован логин;
        if(user.getName() == null || user.getName().isEmpty() ) {
            user.setName(user.getLogin());
        }
        // обновляем пользователя
        users.put(user.getId(), user);
        log.debug("Обновлён пользователь: {}", user);
        return user;
    }
}