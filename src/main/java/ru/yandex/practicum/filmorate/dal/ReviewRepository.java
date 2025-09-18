package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    List<Review> findAllByFilmId(Long filmId, Integer count);

    List<Review> findAll(Integer count);

    Optional<Review> findById(Long id);

    Review save(Review review);

    Review update(Review review);

    void deleteById(Long id);

    void increaseUseful(Integer amount, Long id);

    void decreaseUseful(Integer amount, Long id);

}
