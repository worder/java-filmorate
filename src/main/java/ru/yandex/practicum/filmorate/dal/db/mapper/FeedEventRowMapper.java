package ru.yandex.practicum.filmorate.dal.db.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FeedEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedEventRowMapper implements RowMapper<FeedEvent> {
    @Override
    public FeedEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FeedEvent.builder()
                .eventId(rs.getLong("event_id"))
                .userId(rs.getLong("user_id"))
                .entityId(rs.getLong("entity_id"))
                .eventType(FeedEvent.EventType.valueOf(rs.getString("event_type")))
                .operation(FeedEvent.EventOperation.valueOf(rs.getString("operation")))
                .timestamp(rs.getTimestamp("time_stamp").toInstant())
                .build();
    }
}
