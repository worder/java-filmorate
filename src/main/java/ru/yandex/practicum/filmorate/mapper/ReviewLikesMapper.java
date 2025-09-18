package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.ReviewLikes;

public class ReviewLikesMapper {

    public static ReviewLikes mapToReviewLike(Long reviewId, Long userId, Boolean isLike) {
        return ReviewLikes.builder()
                .reviewId(reviewId)
                .userId(userId)
                .isLike(isLike)
                .build();
    }

}
