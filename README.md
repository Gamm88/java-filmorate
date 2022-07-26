# java-filmorate

Диаграмма планируемой базы данных для filmorate:
![База данных](filmorateDB.png)
Ссылка на диаграмму - https://dbdiagram.io/d/62de18320d66c746553f8aac

### Примеры запросов в БД для основных операций вашего приложения:

#### Получить всех пользователей
SELECT * <br />
FROM user AS u <br />
#### Получить все фильмы, с жанрами и возрастными ограничениями
SELECT * <br />
FROM films AS f <br />
LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id <br />
LEFT JOIN genre AS g ON fg.genre_id = g.genre_id <br />
LEFT JOIN film_mpa AS fm ON f.film_id = fm.film_id <br />
LEFT JOIN mpa AS m ON fm.mpa_id = m.mpa_id <br />
#### Получить топ 10 фильмов
SELECT f.title AS title <br />
       COUNT (l.film_id) AS likes
FROM films AS f <br />
LEFT JOIN likes AS l ON f.film_id = l.film_id <br />
GROUP BY title <br />
ORDER BY likes DESC <br />
LIMIT 10 <br />
#### Получить список общих друзей для пользователей с id = 1 и id = 2
SELECT u.user_id, <br />
       u.name, <br />
       fa.second_friend_id as friend_id, <br />
       u2.name <br />
FROM user AS u <br />
LEFT JOIN friendship AS fa ON u.user_id = fa.first_friend_id <br />
WHERE u.user_id = '1' <br />
      or u.user_id = '2' <br />
HAVING COUNT(friend_id) > 1 <br />