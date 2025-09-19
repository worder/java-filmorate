package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class NewReviewRequest {
    @NotNull
    String content;

    @NotNull
    Boolean isPositive;

    @NotNull
    Long userId;

    @NotNull
    Long filmId;


    Integer useful = 0;
}
