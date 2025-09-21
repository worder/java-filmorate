package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.FeedEvent;

import java.util.List;

public interface FeedEventsRepository {
    FeedEvent save(FeedEvent event);

    List<FeedEvent> findUserFeed(Long userId);
}
