package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull
    private long id;
    @NotNull(message = "Логин не может быть пустым")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Email(message = "Электронная почта указан некорректно")
    @NotNull(message = "Электронная почта не может быть пустой")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
