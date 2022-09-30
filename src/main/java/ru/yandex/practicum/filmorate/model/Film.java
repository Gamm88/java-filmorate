package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exception.DateValidator;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @NotNull
    private long id;
    @NotNull(message = "Название не может быть пустой")
    @NotBlank(message = "Название не может быть пустой")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @DateValidator
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private int duration;
    private Mpa mpa = new Mpa();
    private List<Genre> genres = new ArrayList<>();
    private List<Long> likes = new ArrayList<>();
}
