package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
/**
 * Модель фильма
 */
@Data
public class Film {

    protected int id;

    @NotBlank(message = "Имя не может быть пустой")
    protected String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    protected String description;

    protected LocalDate releaseDate;

    @Positive(message = "Продолжительность не может быть отрицательной")
    protected int duration;
}
