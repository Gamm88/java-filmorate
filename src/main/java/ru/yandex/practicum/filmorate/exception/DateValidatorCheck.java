package ru.yandex.practicum.filmorate.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidatorCheck implements ConstraintValidator<DateValidator, LocalDate> {

    private static final LocalDate RELEASE_DATE_NOT_EARLIER // дата релиза фильмов - не раньше 28 декабря 1895 года
            = LocalDate.of(1895, 12, 25);

    @Override
    public void initialize(DateValidator dateTimeValidator ) {
    }

    @Override
    public boolean isValid(LocalDate filmReleaseDate, ConstraintValidatorContext context) {
        if(filmReleaseDate.isBefore(RELEASE_DATE_NOT_EARLIER)) {
            return false;
        } else {
            return true;
        }
    }
}