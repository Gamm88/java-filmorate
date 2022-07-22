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
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @DateValidator
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность не может быть отрицательной")
    private int duration;

    private Set<Long> likes = new TreeSet<>();
}
