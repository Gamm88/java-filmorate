package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервисы для пользователей
 * добавлять и удалять друзей, поучить список общих друзей
 */
@Slf4j
@Service
@RestControllerAdvice
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // создать пользователя
    public User createUser(User user) {
        if (checkUserEmail(user.getEmail())) {
            throw new NotFoundException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        // имя может быть пустым — в таком случае будет использован логин;
        if (checkUserName(user.getName())) {
            user.setName(user.getLogin());
        }
        log.debug("Добавление пользователя: {}", user);
        return userStorage.createUser(user);
    }

    // получить всех пользователей
    public Collection<User> getAllUsers() {
        log.debug("Получение всех пользователей");
        return userStorage.getAllUsers();
    }

    // получить пользователя по ИД
    public User getUserById(Long userId) {
        if (checkingForExistenceUser(userId)) {
            throw new NotFoundException("Пользователь с ИД=" + userId + " не найден");
        }
        log.debug("Получение пользователя по ИД: {}", userId);
        return userStorage.getUserById(userId);
    }

    // обновление пользователя
    public User updateUser(User user) {
        if (checkingForExistenceUser(user.getId())) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }
        if (checkUserEmail(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        // имя может быть пустым — в таком случае будет использован логин;
        if (checkUserName(user.getName())) {
            user.setName(user.getLogin());
        }
        log.debug("Обновлён пользователь: {}", user);
        return userStorage.updateUser(user);
    }

    // удалить всех пользователей
    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    // удалить пользователя по ИД
    public void deleteUserById(Long userId) {
        if (checkingForExistenceUser(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        log.debug("Удаление пользователя по Ид: {}", userId);
        userStorage.deleteUserById(userId);
    }

    // добавить в друзья
    public User addToFriends(Long userId, Long friendId) {
        if (checkingForExistenceUser(userId) || checkingForExistenceUser(friendId)) {
            throw new NotFoundException("Пользователь и/или его друг не найдены, проверьте ИД");
        }
        log.debug("Пользователь " + userId + " добавляет в друзья пользователя " + friendId);
        userStorage.getUserById(userId).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(userId);
        return userStorage.getUserById(userId);
    }

    // удаление из друзей
    public User removeFromFriends(Long userId, Long friendId) {
        if (checkingForExistenceUser(userId) || checkingForExistenceUser(friendId)) {
            throw new NotFoundException("Пользователь и/или его друг не найдены, проверьте ИД");
        }
        log.debug("Пользователь " + userId + " удаляет из друзей пользователя " + friendId);
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        return userStorage.getUserById(userId);
    }

    // получить список друзей пользователя
    public List<User> getFriendsList(Long userId) {
        // создаём список куда будем добавлять друзей
        List<User> friendsList = new ArrayList<>();
        // наполняем список друзьями из списка ИД друзей пользователя
        for (Long id : userStorage.getUserById(userId).getFriends()) {
            friendsList.add(userStorage.getUserById(id));
        }
        log.debug("Получить список друзей пользователя с ИД: {}", userId);
        return friendsList;
    }

    // получить список общих друзей
    public List<User> getCommonFriends(Long userId, Long otherId) {
        // создаём список куда будем добавлять общих друзей
        List<User> commonFriends = new ArrayList<>();
        // объединяем списки друзей двух пользователей в один список
        List<Long> mergedFriendsList = new ArrayList<>();
        mergedFriendsList.addAll(userStorage.getUserById(userId).getFriends());
        mergedFriendsList.addAll(userStorage.getUserById(otherId).getFriends());
        // оставляем только общих друзей
        mergedFriendsList = mergedFriendsList.stream()
                //группируем в map (пользователь -> количество вхождений)
                .collect(Collectors.groupingBy(Function.identity()))
                //проходим по группам
                .entrySet()
                .stream()
                //отбираем пользователей, встречающихся более одного раза
                .filter(e -> e.getValue().size() > 1)
                //вытаскиваем ключи
                .map(Map.Entry::getKey)
                //собираем в список
                .collect(Collectors.toList());
        // наполняем список друзьями (User) по ИД общих друзей
        for (Long id : mergedFriendsList) {
            commonFriends.add(userStorage.getUserById(id));
        }
        log.debug("Получить список общих друзей пользователя с ИД " + userId + " и с ИД " + otherId);
        return commonFriends;
    }

    // проверка на существование пользователя, по ИД
    private boolean checkingForExistenceUser(Long userId) {
        return userStorage.getUserById(userId) == null;
    }

    // проверка на заполнение имени пользователя
    private boolean checkUserName(String name) {
        return name == null || name.isBlank();
    }

    // проверка на дублирование пользователей (по почте)
    private boolean checkUserEmail(String email) {
        for (User user : userStorage.getAllUsers()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
