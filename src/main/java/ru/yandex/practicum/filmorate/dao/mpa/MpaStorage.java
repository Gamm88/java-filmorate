package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

/**
 * Интерфейс для хранения MPA (возрастных ограничений)
 * и методов получения MPA
 */
public interface MpaStorage {
    // получить все MPA
    List<Mpa> getAllMpa();

    // получить MPA по ИД
    Mpa getMpaById(Long mpaId);
}
