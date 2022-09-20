package ru.yandex.practicum.filmorate.dao.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT genre_id, genre_name FROM genres ORDER BY genre_id";

        List<Genre> genres = jdbcTemplate.query(sql, GenreDbStorage::makeGenre);

        return genres;
    }

    @Override
    public Genre getGenreById(Long genreId) {
        final String sqlQueryFilm = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";

        List<Genre> genres = jdbcTemplate.query(sqlQueryFilm, GenreDbStorage::makeGenre, genreId);

        return genres.get(0);
    }

    static Genre makeGenre(ResultSet rowSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rowSet.getLong("genre_id"))
                .name(rowSet.getString("genre_name"))
                .build();
    }
}
