package ru.yandex.practicum.filmorate.dal.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmLikesRepository;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

@Primary
@Repository("filmLikesDbRepository")
@RequiredArgsConstructor
public class FilmLikesDbRepository implements FilmLikesRepository {
    private static final String INSERT_QUERY = "INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";

    // Запрос для получения всех лайков
    private static final String GET_ALL_LIKES_QUERY = "SELECT user_id, film_id FROM film_likes";

    private final JdbcTemplate db;

    @Override
    public void addLike(Long userId, Long filmId) {
        db.update(INSERT_QUERY, userId, filmId);
    }

    @Override
    public void removeLike(Long userId, Long filmId) {
        db.update(DELETE_QUERY, userId, filmId);
    }

    @Override
    public List<FilmLike> getAllLikes() {
        RowMapper<FilmLike> mapper = (rs, rowNum) -> new FilmLike(
                rs.getLong("user_id"),
                rs.getLong("film_id")
        );
        return db.query(GET_ALL_LIKES_QUERY, mapper);
    }
}
