package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
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

    // добавить в друзья
    public boolean addToFriends(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) != null && userStorage.getUserById(friendId) != null) {
            userStorage.getUserById(userId).getFriends().add(friendId);
            userStorage.getUserById(friendId).getFriends().add(userId);
        } else throw new ValidationException("Неверный пользователя и/или друга");
        log.debug("Пользователь " + userId + " добавляет в друзья пользователя " + friendId);
        return userStorage.getUserById(userId).getFriends().contains(friendId)
                && userStorage.getUserById(friendId).getFriends().contains(userId);
    }

    // удаление из друзей
    public boolean removeFromFriends(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) != null && userStorage.getUserById(friendId) != null) {
            userStorage.getUserById(userId).getFriends().remove(friendId);
            userStorage.getUserById(friendId).getFriends().remove(userId);
        } else throw new ValidationException("Неверный пользователя и/или друга");
        log.debug("Пользователь " + userId + " удаляет из друзей пользователя " + friendId);
        return !userStorage.getUserById(userId).getFriends().contains(friendId)
                && !userStorage.getUserById(friendId).getFriends().contains(userId);
    }

    // получить список друзей пользователя
    public List<User> friendsList(Long userId) {
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
    public List<User> commonFriends(Long userId, Long otherId) {
        // создаём список куда будем добавлять общих друзей
        List<User> commonFriends = new ArrayList<>();
        // объединяем списки друзей 2 пользователей в 1 лист
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
        // наполняем список друзьями по ИД общих друзей
        for (Long id : mergedFriendsList) {
            commonFriends.add(userStorage.getUserById(id));
        }
        log.debug("Получить список общих друзей пользователя с ИД " + userId + " и с ИД " + otherId);
        return commonFriends;
    }
}
