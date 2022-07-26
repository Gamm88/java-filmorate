# java-filmorate

Диаграмма планируемой базы данных для filmorate:
![Диаграмма планируемой базы данных для filmorate](filmorateBD.png)
Ссылка на диаграмму - https://dbdiagram.io/d/62de18320d66c746553f8aac

### Примеры запросов в БД для основных операций вашего приложения:

#### Получить всех пользователей и их друзей
SELECT * <br />
FROM user AS u <br />
LEFT JOIN friendship AS fa ON u.friendship_id = fa.friendship_id  <br />
#### Получить все фильмы c жанрами и возрастными ограничениями
SELECT * <br />
FROM films AS f <br />
LEFT JOIN genre AS g ON f.genre_id = g.genre_id <br />
LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id <br />
#### Получить топ 10 фильмов
SELECT f.title as title <br />
       COUNT(who_likes_id) as likes <br />
FROM films AS f <br />
GROUP BY title <br />
ORDER BY likes DESC <br />
LIMIT 10 <br />
#### Получить список общих друзей для пользователей с id = 1 и id = 2
SELECT u.user_id, <br />
       u.name, <br />
       fa.second_friend_id, <br />
       u2.name <br />
FROM user AS u <br />
LEFT JOIN friendship AS fa ON u.friendship_id = fa.friendship_id <br />
LEFT JOIN user AS u2 ON fa.second_friend_id = u2.user_id <br />
WHERE u.user_id = '1' <br />
      or u.user_id = '2' <br />
HAVING COUNT(fa.second_friend_id) > 1 <br />