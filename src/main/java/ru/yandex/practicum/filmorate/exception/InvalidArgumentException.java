package ru.yandex.practicum.filmorate.exception;

public class InvalidArgumentException extends RuntimeException {
  public InvalidArgumentException(String message) {
    super(message);
  }
}
