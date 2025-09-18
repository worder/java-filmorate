package ru.yandex.practicum.filmorate.dto.feed;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FeedEventDto {
    Long timestamp;
    Long userId;
    String eventType;
    String operation;
    Long eventId;
    Long entityId;
}
