package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.ReviewLikes;

import java.util.Optional;

public interface ReviewLikesRepository {

    Optional<ReviewLikes> findByReviewIdUserId(Long reviewId, Long userId);

    void save(ReviewLikes reviewLikes);

    void update(ReviewLikes reviewLikes);

    void deleteByReviewIdUserId(Long reviewId, Long userId);
}
