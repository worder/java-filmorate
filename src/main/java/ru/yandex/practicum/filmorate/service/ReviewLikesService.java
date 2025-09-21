package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.ReviewLikesRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewLikesMapper;
import ru.yandex.practicum.filmorate.model.ReviewLikes;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewLikesService {
    private final ReviewLikesRepository storage;
    private final ReviewService reviewService;
    private final UserService userService;


    public void createReviewLikes(Long reviewId, Long userId, Boolean isLike) {
        if (!reviewService.isReviewExists(reviewId)) {
            throw new NotFoundException("Review with id=" + reviewId + " not found");
        }

        if (!userService.userExists(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }

        ReviewLikes newReviewLike = ReviewLikesMapper.mapToReviewLike(reviewId, userId, isLike);

        Optional<ReviewLikes> existingReviewLike = storage.findByReviewIdUserId(reviewId, userId);

        if (existingReviewLike.isPresent() && existingReviewLike.get().getIsLike().equals(newReviewLike.getIsLike())) {
            return;
        }

        storage.save(newReviewLike);

        if (existingReviewLike.isPresent()) {
            if (isLike.equals(true)) {
                reviewService.increaseReviewUseful(2, reviewId);
            } else {
                reviewService.decreaseReviewUseful(2, reviewId);
            }
        } else {
            if (isLike.equals(true)) {
                reviewService.increaseReviewUseful(1, reviewId);
            } else {
                reviewService.decreaseReviewUseful(1, reviewId);
            }
        }
    }


    public void deleteReview(Long reviewId, Long userId) {

        Optional<ReviewLikes> reviewLikes = this.getReviewLikes(reviewId, userId);

        if (!reviewLikes.isPresent()) {
            throw new NotFoundException("Review Like not found");
        }

        storage.deleteByReviewIdUserId(reviewId, userId);

        if (reviewLikes.get().getIsLike().equals(true)) {
            reviewService.decreaseReviewUseful(1, reviewId);
        } else {
            reviewService.increaseReviewUseful(1, reviewId);
        }
    }

    public Optional<ReviewLikes> getReviewLikes(Long reviewId, Long userId) {
        return storage.findByReviewIdUserId(reviewId, userId);
    }

}
