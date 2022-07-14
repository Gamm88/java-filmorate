package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.DateValidator;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

/**
 * Модель фильма
 */
@Data
public class Film {

    private long id;

    @NotBlank(message = "Имя не может быть пустой")
    protected String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    protected String description;

    @DateValidator
    protected LocalDate releaseDate;

    @Positive(message = "Продолжительность не может быть отрицательной")
    protected int duration;

    protected Set<Long> likes = new TreeSet<>();
}
