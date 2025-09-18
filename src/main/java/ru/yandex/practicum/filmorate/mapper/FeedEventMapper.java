package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.feed.FeedEventDto;
import ru.yandex.practicum.filmorate.model.FeedEvent;

public class FeedEventMapper {
    public static FeedEventDto mapToFeedEventDto(FeedEvent event) {
        return FeedEventDto.builder()
                .timestamp(event.getTimestamp().getEpochSecond())
                .userId(event.getUserId())
                .eventType(event.getEventType().toString())
                .operation(event.getOperation().toString())
                .eventId(event.getEventId())
                .entityId(event.getEntityId())
                .build();
    }
}
