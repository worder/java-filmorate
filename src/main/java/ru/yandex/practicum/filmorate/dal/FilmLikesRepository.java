package ru.yandex.practicum.filmorate.dal;

public interface FilmLikesRepository {
    void addLike(Long userId, Long filmId);

    void removeLike(Long userId, Long filmId);
}
