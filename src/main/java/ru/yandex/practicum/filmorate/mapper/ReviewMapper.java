package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;

public class ReviewMapper {
    public static ReviewDto mapToReviewDto(Review review) {

        return ReviewDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .useful(review.getUseful())
                .build();
    }

    public static Review mapToReview(NewReviewRequest request) {
        return Review.builder()
                .content(request.getContent())
                .isPositive(request.getIsPositive())
                .userId(request.getUserId())
                .filmId(request.getFilmId())
                .useful(request.getUseful())
                .build();
    }

    public static Review updateReviewFields(Review review, UpdateReviewRequest request) {
        Review.ReviewBuilder reviewBuilder = review.toBuilder();

        if (request.hasContent()) {
            reviewBuilder.content(request.getContent());
        }

        reviewBuilder.isPositive(request.getIsPositive());

        return reviewBuilder.build();
    }

}
