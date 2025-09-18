package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@ToString
public class ReviewLikes {
    Long reviewId;
    Long userId;
    Boolean isLike;
}
