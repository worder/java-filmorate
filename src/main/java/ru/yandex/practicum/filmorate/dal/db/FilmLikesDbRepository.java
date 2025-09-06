package ru.yandex.practicum.filmorate.dal.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmLikesRepository;

@Repository
@RequiredArgsConstructor
public class FilmLikesDbRepository implements FilmLikesRepository {
    private static final String INSERT_QUERY = "INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";

    private final JdbcTemplate db;

    @Override
    public void addLike(Long userId, Long filmId) {
        db.update(INSERT_QUERY, userId, filmId);
    }

    @Override
    public void removeLike(Long userId, Long filmId) {
        db.update(DELETE_QUERY, userId, filmId);
    }
}
