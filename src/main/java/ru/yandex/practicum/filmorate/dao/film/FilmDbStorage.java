package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        final String sqlQueryFilm = "" +
                "INSERT INTO films (film_name, description, release_date, duration, film_mpa) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryFilm, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));

        for (Genre genre : film.getGenres()) {
            final String sqlQueryGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";
            jdbcTemplate.update(sqlQueryGenre, film.getId(), genre.getId());
        }

        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        final String sqlQuery = "" +
                "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, f.film_mpa, m.mpa_name " +
                "FROM films AS f " +
                "JOIN mpa AS m ON f.film_mpa = m.mpa_id";

        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
        films.forEach(film -> film.setGenres((getFilmGenres(film.getId()))));
        films.forEach(film -> film.setLikes((getFilmLikes(film.getId()))));

        return films;
    }

    @Override
    public Film getFilmById(Long filmId) {
        final String sqlQueryFilm = "" +
                "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, f.film_mpa, m.mpa_name " +
                "FROM films AS f " +
                "JOIN mpa AS m ON f.film_mpa = m.mpa_id " +
                "WHERE f.film_id = ?";

        List<Film> films = jdbcTemplate.query(sqlQueryFilm, FilmDbStorage::makeFilm, filmId);
        films.forEach(film -> film.setGenres((getFilmGenres(film.getId()))));
        films.forEach(film -> film.setLikes((getFilmLikes(film.getId()))));

        return films.get(0);
    }

    @Override
    public Film updateFilm(Film film) {
        final String sqlQuery = "" +
                "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, film_mpa = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        film.setLikes((getFilmLikes(film.getId())));

        final String sqlDelete = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlDelete, film.getId());

        for (Genre genre : film.getGenres()) {
            final String sqlQueryGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";
            jdbcTemplate.update(sqlQueryGenre, film.getId(), genre.getId());
        }

        return film;
    }

    @Override
    public void deleteAllFilms() {
        final String sqlQuery = "DELETE films";

        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public void deleteFilmById(Long filmId) {
        final String sqlQuery = "DELETE FROM films WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        final String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        final String sqlQuery = "" +
                "SELECT * FROM films AS F " +
                "LEFT JOIN mpa AS m on f.film_mpa = m.mpa_id " +
                "LEFT JOIN likes AS l on f.film_id = l.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC LIMIT ?";

        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, count);
        films.forEach(film -> film.setGenres((getFilmGenres(film.getId()))));
        films.forEach(film -> film.setLikes((getFilmLikes(film.getId()))));

        return films;
    }

    private List<Genre> getFilmGenres(Long filmId) {
        final String sqlQuery = "" +
                "SELECT g.genre_id, g.genre_name " +
                "FROM film_genre AS fg " +
                "JOIN genres AS g ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";

        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeGenre, filmId);
    }

    private List<Long> getFilmLikes(Long filmId) {
        final String sqlQuery = "" +
                "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";

        return jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
    }

    static Film makeFilm(ResultSet resultSet, long rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getLong("film_mpa"), resultSet.getString("mpa_name")))
                .build();
    }

    static Genre makeGenre(ResultSet rowSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rowSet.getLong("genre_id"))
                .name(rowSet.getString("genre_name"))
                .build();
    }
}
