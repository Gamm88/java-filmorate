package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.List;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

/**
 * Сервисы для пользователей
 * добавлять и удалять друзей, поучить список общих друзей
 */
@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // создать пользователя
    public User createUser(User user) {
        checkUserEmail(user.getEmail());
        // имя может быть пустым — в таком случае будет использован логин;
        if (checkUserName(user.getName())) {
            user.setName(user.getLogin());
        }
        log.info("Добавление пользователя: {}", user);
        return userStorage.createUser(user);
    }

    // получить всех пользователей
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userStorage.getAllUsers();
    }

    // получить пользователя по ИД
    public User getUserById(Long userId) {
        checkingForExistenceUser(userId);
        log.info("Получение пользователя по ИД: {}", userId);
        return userStorage.getUserById(userId);
    }

    // обновление пользователя
    public User updateUser(User user) {
        checkingForExistenceUser(user.getId());
        checkUserEmail(user.getEmail());
        // имя может быть пустым — в таком случае будет использован логин;
        if (checkUserName(user.getName())) {
            user.setName(user.getLogin());
        }
        log.info("Обновлён пользователь: {}", user);
        return userStorage.updateUser(user);
    }

    // удалить всех пользователей
    public void deleteAllUsers() {
        log.info("Удаление пользователя всех пользователей");
        userStorage.deleteAllUsers();
    }

    // удалить пользователя по ИД
    public void deleteUserById(Long userId) {
        checkingForExistenceUser(userId);
        log.info("Удаление пользователя по ИД: {}", userId);
        userStorage.deleteUserById(userId);
    }

    // добавить в друзья
    public void addToFriends(Long userId, Long friendId) {
        checkingForExistenceUser(userId);
        checkingForExistenceUser(friendId);
        log.info("Пользователь " + userId + " добавляет в друзья пользователя " + friendId);
        userStorage.addToFriends(userId, friendId);
    }

    // удаление из друзей
    public void removeFromFriends(Long userId, Long friendId) {
        checkingForExistenceUser(userId);
        checkingForExistenceUser(friendId);
        log.info("Пользователь " + userId + " удаляет из друзей пользователя " + friendId);
        userStorage.removeFromFriends(userId, friendId);
    }

    // получить список друзей пользователя, по ИД
    public List<User> getFriendsList(Long userId) {
        checkingForExistenceUser(userId);
        log.info("Получить список друзей пользователя с ИД: {}", userId);
        return userStorage.getUserFriends(userId);
    }

    // получить список общих друзей
    public List<User> getCommonFriends(Long userId, Long otherId) {
        checkingForExistenceUser(userId);
        checkingForExistenceUser(otherId);
        log.info("Получить список общих друзей пользователя с ИД " + userId + " и с ИД " + otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }

    // проверка на существование пользователя, по ИД
    private void checkingForExistenceUser(Long userId) {
        if (userId < 0 || userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }
    }

    // проверка на дублирование пользователей, по почте
    private void checkUserEmail(String email) {
        for (User user : userStorage.getAllUsers()) {
            if (user.getEmail().equals(email)) {
                throw new ValidationException("Пользователь с электронной почтой " + email + " уже зарегистрирован.");
            }
        }
    }

    // проверка на заполнение имени пользователя
    private boolean checkUserName(String name) {
        return name == null || name.isBlank();
    }
}
