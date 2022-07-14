package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение на корректность вводимых данных
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}