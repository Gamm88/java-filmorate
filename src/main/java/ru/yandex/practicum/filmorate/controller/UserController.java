package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * Контроллер пользователей
 * содержит эндпоинты
 */
@RestController
@RestControllerAdvice
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    // создать пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.createUser(user);
    }

    // получить всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    // получить пользователя по ИД
    @GetMapping("/{userId}")
    public User getFilmById(@PathVariable("userId") long userId) {
        return inMemoryUserStorage.getUserById(userId);
    }

    // обновить пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    // удалить всех пользователей
    @DeleteMapping
    public boolean deleteAllUsers() {
        return inMemoryUserStorage.deleteAllUsers();
    }

    // удалить пользователя по ИД
    @DeleteMapping("/{userId}")
    public boolean deleteUserById(@PathVariable("userId") long userId) {
        return inMemoryUserStorage.deleteUserById(userId);
    }

    // добавить в друзья
    @PutMapping("/{userId}/friends/{friendId}")
    public boolean addToFriends(@PathVariable long userId, @PathVariable long friendId) {
        return userService.addToFriends(userId, friendId);
    }

    // удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public boolean removeFromFriends(@PathVariable long userId, @PathVariable long friendId) {
        return userService.removeFromFriends(userId, friendId);
    }

    // получить список друзей пользователя
    @GetMapping("/{userId}/friends")
    public List<User> friendsList(@PathVariable long userId) {
        return userService.friendsList(userId);
    }

    // получить список общих друзей
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.commonFriends(userId, otherId);
    }
}