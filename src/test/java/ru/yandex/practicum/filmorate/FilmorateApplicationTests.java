package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;

import javax.transaction.Transactional;
import java.util.*;

import static java.time.LocalDate.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    private final FriendshipStorage friendshipStorage;

    @Test
    @Transactional
    void testGetUserById() {
        User newUser = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(now().minusYears(34))
                .build();

        User createdUser = userStorage.createUser(newUser);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(createdUser.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrProperty("id");
                            assertThat(user).hasFieldOrPropertyWithValue("login", "dolore");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Nick Name");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "mail@mail.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", now().minusYears(34));
                        }
                );
    }

    @Test
    @Transactional
    void testGetAllUsers() {
        User newUser = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(now().minusYears(34))
                .build();

        User newUser2 = User.builder()
                .login("friend")
                .name("friend adipisicing")
                .email("friend@mail.ru")
                .birthday(now().minusYears(30))
                .build();

        userStorage.createUser(newUser);
        userStorage.createUser(newUser2);

        List<User> users = userStorage.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    @Transactional
    void testUpdateUser() {
        User newUser = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(now().minusYears(34))
                .build();

        User createdUser = userStorage.createUser(newUser);

        User userForUpdate = User.builder()
                .id(createdUser.getId())
                .login("doloreUpdate")
                .name("est adipisicing")
                .email("mail@yandex.ru")
                .birthday(now().minusYears(30))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(userForUpdate));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", createdUser.getId());
                            assertThat(user).hasFieldOrPropertyWithValue("login", "doloreUpdate");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "est adipisicing");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "mail@yandex.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", now().minusYears(30));
                        }
                );
    }

    @Test
    @Transactional
    void testAddFriendAndDeleteFriend() {
        var user = userStorage.createUser(
                User.builder()
                        .login("dolore")
                        .name("Nick Name")
                        .email("mail@mail.ru")
                        .birthday(now().minusYears(34))
                        .build());

        var friend = userStorage.createUser(
                User.builder()
                        .login("friend")
                        .name("friend adipisicing")
                        .email("friend@mail.ru")
                        .birthday(now().minusYears(30))
                        .build());

        friendshipStorage.addToFriends(user.getId(), friend.getId());
        var userFriends = userStorage.getUserFriends(user.getId());
        assertEquals(friend.getId(), userFriends.get(0).getId());

        friendshipStorage.removeFromFriends(user.getId(), friend.getId());
        userFriends = userStorage.getUserFriends(user.getId());
        assertTrue(userFriends.isEmpty());
    }

    @Test
    @Transactional
    void testGetFilmById() {
        Film newFilm = Film.builder()
                .name("Labore nulla")
                .description("Duis in consequat esse")
                .releaseDate(now().minusYears(50))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(new ArrayList<>())
                .build();

        Film createdFilm = filmStorage.createFilm(newFilm);

        var filmOptional = Optional.ofNullable(filmStorage.getFilmById(createdFilm.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrProperty("id");
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Labore nulla");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "Duis in consequat esse");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", now().minusYears(50));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 100);
                            assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1L, "G"));
                        }
                );
    }

    @Test
    @Transactional
    void testUpdateFilm() {
        Film newFilm = Film.builder()
                .name("Labore nulla")
                .description("Duis in consequat esse")
                .releaseDate(now().minusYears(50))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(new ArrayList<>())
                .build();

        Film createdFilm = filmStorage.createFilm(newFilm);

        Film filmForUpdate = Film.builder()
                .id(createdFilm.getId())
                .name("Film Updated")
                .description("New film update description")
                .releaseDate(now().minusYears(50))
                .duration(190)
                .mpa(new Mpa(5L, "NC-17"))
                .genres(new ArrayList<>())
                .build();

        var optionalFilm = Optional.ofNullable(filmStorage.updateFilm(filmForUpdate));

        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", createdFilm.getId());
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Film Updated");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "New film update description");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", now().minusYears(50));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 190);
                            assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(5L, "NC-17"));

                        }
                );
    }


    @Test
    @Transactional
    void testGetGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getGenreById(1L));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                            assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
                        }
                );
    }

    @Test
    @Transactional
    void testGetMpaById() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpaById(1L));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }
}