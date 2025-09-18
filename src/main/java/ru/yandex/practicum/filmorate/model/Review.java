package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@ToString
public class Review {
    Long id;
    String content;
    Boolean isPositive;
    Long userId;
    Long filmId;
    Integer useful;
}
