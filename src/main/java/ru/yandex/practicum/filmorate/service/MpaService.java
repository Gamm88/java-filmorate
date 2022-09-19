package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getAllMpa() {
        log.info("Получение списка mpa");
        return mpaDbStorage.getAllMpa();
    }

    public Mpa getMpaById(Long MpaId) {
        checkingForExistenceMpa(MpaId);
        log.info("Получение mpa по Ид: {}", MpaId);
        return mpaDbStorage.getMpaById(MpaId);
    }

    private void checkingForExistenceMpa(Long mpaId) {
        if (mpaId < 0 || mpaDbStorage.getMpaById(mpaId) == null) {
            throw new NotFoundException("Mpa с ИД " + mpaId + " не найден");
        }
    }
}
