package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {
    @NotNull
    Long id;
    @NotNull(message = "Название не может быть пустой")
    @NotBlank(message = "Название не может быть пустой")
    String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mpa mpa = (Mpa) o;
        return Objects.equals(id, mpa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
