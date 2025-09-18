package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewLikesService;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewLikesService reviewLikesService;

    @GetMapping("/{id}")
    public ReviewDto getById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public Collection<ReviewDto> getAllByFilmId(
            @RequestParam(required = false) Long filmId,
            @RequestParam(required = false, defaultValue = "10") Integer count
    ) {
        return reviewService.getAllReviews(filmId, count);
    }

    @PostMapping
    public ReviewDto create(@Valid @RequestBody NewReviewRequest review) {
        return reviewService.createReview(review);
    }

    @PutMapping
    public ReviewDto update(@Valid @RequestBody UpdateReviewRequest review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewLikesService.createReviewLikes(id, userId, true);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewLikesService.createReviewLikes(id, userId, false);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewLikesService.deleteReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeFromReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewLikesService.deleteReview(id, userId);
    }


}
