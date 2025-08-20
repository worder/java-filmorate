package ru.yandex.practicum.filmorate.error;

import lombok.Value;

@Value
public class ErrorResponse {
    String error;
    String description;
}
