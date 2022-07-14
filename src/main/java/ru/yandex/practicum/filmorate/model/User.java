package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

/**
 * Модель пользователя
 */
@Data
public class User {

    private long id;

    @NotBlank(message = "Логин не может быть пустой")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    protected String login;

    protected String name;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта указан некорректно")
    protected String email;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    protected LocalDate birthday;

    protected Set<Long> friends = new TreeSet<>();
}
