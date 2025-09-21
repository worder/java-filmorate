package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FeedEventsRepository;
import ru.yandex.practicum.filmorate.dto.feed.FeedEventDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FeedEventMapper;
import ru.yandex.practicum.filmorate.model.FeedEvent;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedEventsRepository eventsStorage;
    private final UserService userService;

    public void addEvent(FeedEvent event) {
        FeedEvent newEvent = eventsStorage.save(event);
        log.info("Added feed event: {}", newEvent);
    }

    public List<FeedEventDto> getUserFeed(Long userId) {
        if (!this.userService.userExists(userId)) {
            throw new NotFoundException("User not found");
        }

        return eventsStorage.findUserFeed(userId).stream()
                .map(FeedEventMapper::mapToFeedEventDto)
                .toList();
    }
}
