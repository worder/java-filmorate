package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.ReviewLikesRepository;
import ru.yandex.practicum.filmorate.model.ReviewLikes;

import java.util.Optional;

@Primary
@Repository("reviewLikesDbRepository")
public class ReviewLikesDbRepository extends BaseDbRepositoryMapper<ReviewLikes> implements ReviewLikesRepository {

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM review_likes
            WHERE review_id = ? AND user_id = ?
            """;

    private static final String INSERT_REVIEW_LIKES_QUERY = """
            INSERT INTO review_likes (review_id, user_id, is_like)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_REVIEW_LIKES_QUERY = """
            UPDATE review_likes
            SET is_like = ?
            WHERE review_id = ? AND user_id = ?
            """;
    private static final String DELETE_REVIEW_LIKES_QUERY = """
            DELETE review_likes
            WHERE review_id = ? AND user_id = ?
            """;

    private static final String MERGE_REVIEW_LIKES_QUERY = """
            MERGE INTO review_likes (review_id, user_id, is_like) KEY (review_id, user_id) VALUES (?, ?, ?)
            """;

    public ReviewLikesDbRepository(JdbcTemplate jdbc, RowMapper<ReviewLikes> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<ReviewLikes> findByReviewIdUserId(Long reviewId, Long userId) {
        return this.findOne(FIND_BY_ID_QUERY, reviewId, userId);
    }

    @Override
    public void save(ReviewLikes reviewLikes) {
        this.insertWithoutId(
                MERGE_REVIEW_LIKES_QUERY,
                reviewLikes.getReviewId(),
                reviewLikes.getUserId(),
                reviewLikes.getIsLike()
        );
    }

    @Override
    public void update(ReviewLikes reviewLikes) {
        this.update(
                UPDATE_REVIEW_LIKES_QUERY,
                reviewLikes.getIsLike(),
                reviewLikes.getReviewId(),
                reviewLikes.getUserId()
        );
    }

    @Override
    public void deleteByReviewIdUserId(Long reviewId, Long userId) {
        this.update(
                DELETE_REVIEW_LIKES_QUERY,
                reviewId,
                userId
        );
    }
}
