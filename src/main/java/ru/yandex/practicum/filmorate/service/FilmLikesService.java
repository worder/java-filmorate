package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmLikesRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FeedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmLikesService {
    private final UserService userService;
    private final FilmService filmService;
    private final FeedService feedService;
    private final FilmLikesRepository storage;

    public void addLike(Long userId, Long filmId) {
        if (storage.likeExists(userId, filmId)) {
            return;
        }
        if (filmService.filmExists(filmId) && userService.userExists(userId)) {
            log.info("Added like for film: {} from user: {}", filmId, userId);
            storage.addLike(userId, filmId);
            feedService.addEvent(FeedEvent.addLike(userId, filmId));
        } else {
            throw new NotFoundException("");
        }
    }

    public void removeLike(Long userId, Long filmId) {
        if (filmService.filmExists(filmId) && userService.userExists(userId)) {
            log.info("Removed like for film: {} from user: {}", filmId, userId);
            storage.removeLike(userId, filmId);
            feedService.addEvent(FeedEvent.removeLike(userId, filmId));
        } else {
            throw new NotFoundException("");
        }
    }
}
