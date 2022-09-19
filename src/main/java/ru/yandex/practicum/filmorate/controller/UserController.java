package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер пользователей
 * содержит эндпоинты
 */
@RestController
@RestControllerAdvice
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // создать пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    // получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // получить пользователя по ИД
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    // обновить пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    // удалить всех пользователей
    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    // удалить пользователя по ИД
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }

    // добавить в друзья
    @PutMapping("/{userId}/friends/{friendId}")
    public void addToFriends(@PathVariable long userId, @PathVariable Long friendId) {
        userService.addToFriends(userId, friendId);
    }

    // удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFromFriends(userId, friendId);
    }

    // получить список друзей пользователя
    @GetMapping("/{userId}/friends")
    public List<User> getFriendsList(@PathVariable Long userId) {
        return userService.getFriendsList(userId);
    }

    // получить список общих друзей
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}