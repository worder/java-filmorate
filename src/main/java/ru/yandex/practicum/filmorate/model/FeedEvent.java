package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
@ToString
public class FeedEvent {
    public enum EventType {
        LIKE,
        REVIEW,
        FRIEND
    }

    public enum EventOperation {
        REMOVE,
        ADD,
        UPDATE
    }

    Long eventId;
    Long userId;
    Long entityId;
    EventType eventType;
    EventOperation operation;
    Instant timestamp;

    public static FeedEvent addLike(Long userId, Long filmId) {
        return FeedEvent.builder()
                .userId(userId)
                .entityId(filmId)
                .eventType(EventType.LIKE)
                .operation(EventOperation.ADD)
                .build();
    }

    public static FeedEvent removeLike(Long userId, Long filmId) {
        return FeedEvent.builder()
                .userId(userId)
                .entityId(filmId)
                .eventType(EventType.LIKE)
                .operation(EventOperation.REMOVE)
                .build();
    }

    public static FeedEvent addFriend(Long userId, Long friendId) {
        return FeedEvent.builder()
                .userId(userId)
                .entityId(friendId)
                .eventType(EventType.FRIEND)
                .operation(EventOperation.ADD)
                .build();
    }

    public static FeedEvent removeFriend(Long userId, Long friendId) {
        return FeedEvent.builder()
                .userId(userId)
                .entityId(friendId)
                .eventType(EventType.FRIEND)
                .operation(EventOperation.REMOVE)
                .build();
    }

    public static FeedEvent addReview(Long userId, Long filmId) {
        return FeedEvent.builder()
                .userId(userId)
                .entityId(filmId)
                .eventType(EventType.REVIEW)
                .operation(EventOperation.ADD)
                .build();
    }

    public static FeedEvent removeReview(Long userId, Long filmId) {
        return FeedEvent.builder()
                .userId(userId)
                .entityId(filmId)
                .eventType(EventType.REVIEW)
                .operation(EventOperation.REMOVE)
                .build();
    }
}
