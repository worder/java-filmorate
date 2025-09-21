package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Primary
@Repository("reviewDbRepository")
public class ReviewDbRepository extends BaseDbRepositoryMapper<Review> implements ReviewRepository {
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM reviews
            ORDER BY useful DESC
            LIMIT ?
            """;

    private static final String FIND_ALL_BY_FILM_ID = """
            SELECT *
            FROM reviews
            WHERE film_id = ?
            ORDER BY useful DESC
            LIMIT ?
            """;

    private static final String INSERT_REVIEW_QUERY = """
            INSERT INTO reviews (content, is_positive, user_id, film_id, useful)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM reviews WHERE id = ?";
    private static final String UPDATE_REVIEW_QUERY = """
            UPDATE reviews
            SET content = ?, is_positive = ?, useful = ?
            WHERE id = ?
            """;
    private static final String DELETE_REVIEW_QUERY = """
            DELETE reviews WHERE id = ?
            """;

    private static final String INCREASE_REVIEW_USEFUL_QUERY = """
            UPDATE reviews
            SET useful = useful + ?
            WHERE id = ?
            """;

    private static final String DECREASE_REVIEW_USEFUL_QUERY = """
            UPDATE reviews
            SET useful = useful - ?
            WHERE id = ?
            """;

    public ReviewDbRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Review> findAllByFilmId(Long filmId, Integer count) {
        return this.findMany(FIND_ALL_BY_FILM_ID, filmId, count);
    }

    @Override
    public List<Review> findAll(Integer count) {
        return this.findMany(FIND_ALL_QUERY, count);
    }


    @Override
    public Optional<Review> findById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Review save(Review review) {
        long id = this.insert(
                INSERT_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful()
        );
        return review.toBuilder().id(id).build();
    }

    @Override
    public Review update(Review review) {
        this.update(
                UPDATE_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUseful(),
                review.getId()
        );
        return review;
    }

    @Override
    public void deleteById(Long id) {
        this.update(
                DELETE_REVIEW_QUERY,
                id
        );
    }

    @Override
    public void increaseUseful(Integer amount, Long id) {
        this.update(
                INCREASE_REVIEW_USEFUL_QUERY,
                amount,
                id
        );
    }

    @Override
    public void decreaseUseful(Integer amount, Long id) {
        this.update(
                DECREASE_REVIEW_USEFUL_QUERY,
                amount,
                id
        );
    }


}
