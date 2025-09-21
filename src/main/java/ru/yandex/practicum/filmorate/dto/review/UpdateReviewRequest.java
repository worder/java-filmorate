package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UpdateReviewRequest {

    Long reviewId;

    String content;

    @NotNull
    Boolean isPositive;

    @NotNull
    Long userId;

    @NotNull
    Long filmId;

    public boolean hasContent() {
        return !(content == null || content.isBlank());
    }

}
