package ru.yandex.practicum.filmorate.dao.Friendship;

/**
 * Интерфейс для добавления и удаления в друзья пользователей
 */
public interface FriendshipStorage {
    // добавление в друзья
    void addToFriends(Long userId, Long friendId);

    // удаление из друзей
    void removeFromFriends(Long userId, Long friendId);
}