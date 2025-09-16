package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

public interface FilmLikesRepository {
    void addLike(Long userId, Long filmId);

    void removeLike(Long userId, Long filmId);

    // Метод для получения всех лайков из базы данных
    List<FilmLike> getAllLikes();
}
