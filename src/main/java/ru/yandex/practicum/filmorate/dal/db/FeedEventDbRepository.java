package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FeedEventsRepository;
import ru.yandex.practicum.filmorate.model.FeedEvent;

import java.time.Instant;
import java.util.List;

@Primary
@Repository
public class FeedEventDbRepository extends BaseDbRepositoryMapper<FeedEvent> implements FeedEventsRepository {

    private static final String INSERT_EVENT_QUERY = """
            INSERT INTO feed_events (user_id, entity_id, time_stamp, event_type, operation)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String FIND_USER_FEED_QUERY = """
            SELECT *
            FROM feed_events
            WHERE user_id = ?
            ORDER BY time_stamp
            """;

    FeedEventDbRepository(JdbcTemplate db, RowMapper<FeedEvent> mapper) {
        super(db, mapper);
    }

    @Override
    public FeedEvent save(FeedEvent event) {
        FeedEvent newEvent = event.toBuilder().timestamp(Instant.now().toEpochMilli()).build();
        Long id = this.insert(INSERT_EVENT_QUERY,
                newEvent.getUserId(),
                newEvent.getEntityId(),
                newEvent.getTimestamp(),
                newEvent.getEventType().toString(),
                newEvent.getOperation().toString());

        return newEvent.toBuilder().eventId(id).build();
    }

    @Override
    public List<FeedEvent> findUserFeed(Long userId) {
        return this.findMany(FIND_USER_FEED_QUERY, userId);
    }
}
