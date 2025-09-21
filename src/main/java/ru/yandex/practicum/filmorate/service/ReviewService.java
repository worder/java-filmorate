package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository storage;
    private final FilmService filmService;
    private final UserService userService;
    private final FeedService feedService;

    public List<ReviewDto> getAllReviews(Long filmId, Integer count) {

        if (filmId != null) {

            if (!filmService.filmExists(filmId)) {
                throw new NotFoundException("Film with id=" + filmId + " not found");
            }

            return storage.findAllByFilmId(filmId, count).stream()
                    .map(ReviewMapper::mapToReviewDto)
                    .toList();
        }
        return storage.findAll(count).stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }

    public ReviewDto getReviewById(Long id) {
        Review review = storage.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        return ReviewMapper.mapToReviewDto(review);
    }

    public ReviewDto createReview(NewReviewRequest request) {
        if (!filmService.filmExists(request.getFilmId())) {
            throw new NotFoundException("Film with id=" + request.getFilmId() + " not found");
        }

        if (!userService.userExists(request.getUserId())) {
            throw new NotFoundException("User with id=" + request.getUserId() + " not found");
        }


        Review newReview = ReviewMapper.mapToReview(request);

        newReview = storage.save(newReview);
        log.info("Created review: {} from data: {}", newReview, request);
        feedService.addEvent(FeedEvent.addReview(newReview.getUserId(), newReview.getId()));

        return ReviewMapper.mapToReviewDto(newReview);
    }

    public ReviewDto updateReview(UpdateReviewRequest request) {
        if (!this.isReviewExists(request.getReviewId())) {
            throw new NotFoundException("Review with id=" + request.getReviewId() + " not found");
        }

        if (!filmService.filmExists(request.getFilmId())) {
            throw new NotFoundException("Film with id=" + request.getFilmId() + " not found");
        }

        if (!userService.userExists(request.getUserId())) {
            throw new NotFoundException("User with id=" + request.getUserId() + " not found");
        }

        Review review = storage.findById(request.getReviewId())
                .map(rev -> ReviewMapper.updateReviewFields(rev, request))
                .orElseThrow(() -> new NotFoundException("Review update failed, review not found"));

        review = storage.update(review);
        log.info("Updated review: {} from data: {}", review, request);
        feedService.addEvent(FeedEvent.updateReview(review.getUserId(), review.getId()));

        return ReviewMapper.mapToReviewDto(review);
    }


    public void deleteReview(Long id) {
        Review review = storage.findById(id)
                .orElseThrow(() -> new NotFoundException("Review with id=" + id + " not found"));

        storage.deleteById(id);
        log.info("Deleted review: {} from data: {}", id, this);
        feedService.addEvent(FeedEvent.removeReview(review.getUserId(), review.getId()));
    }


    public boolean isReviewExists(Long id) {
        return storage.findById(id).isPresent();
    }

    public void increaseReviewUseful(Integer amount, Long id) {
        storage.increaseUseful(amount, id);
    }

    public void decreaseReviewUseful(Integer amount, Long id) {
        storage.decreaseUseful(amount, id);
    }


}
