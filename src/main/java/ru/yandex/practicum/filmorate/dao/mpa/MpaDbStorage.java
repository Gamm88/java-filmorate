package ru.yandex.practicum.filmorate.dao.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT mpa_id, mpa_name FROM mpa ORDER BY mpa_id";

        List<Mpa> mpa = jdbcTemplate.query(sql, MpaDbStorage::makeMpa);

        return mpa;
    }

    @Override
    public Mpa getMpaById(Long mpaId) {
        final String sqlQueryFilm = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";

        List<Mpa> mpa = jdbcTemplate.query(sqlQueryFilm, MpaDbStorage::makeMpa, mpaId);

        return mpa.get(0);
    }

    static Mpa makeMpa(ResultSet rowSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rowSet.getLong("mpa_id"))
                .name(rowSet.getString("mpa_name"))
                .build();
    }
}
