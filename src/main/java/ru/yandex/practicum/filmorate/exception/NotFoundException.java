package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение если искомый объект не найден
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }
}