package ru.yandex.practicum.filmorate.exception;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidatorCheck.class)
@Documented
public @interface DateValidator {

    String message() default "Дата релиза фильма — не раньше 28 декабря 1895 года.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}