package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
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
