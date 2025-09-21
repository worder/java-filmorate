package ru.yandex.practicum.filmorate.dto.review;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ReviewDto {
    Long reviewId;
    String content;
    Boolean isPositive;
    Long userId;
    Long filmId;
    Integer useful;
}
