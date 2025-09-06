package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmLikesRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class FilmLikesService {
    private final UserService userService;
    private final FilmService filmService;
    private final FilmLikesRepository storage;

    public void addLike(Long userId, Long filmId) {
        if (filmService.filmExists(filmId) && userService.userExists(userId)) {
            storage.addLike(userId, filmId);
        } else {
            throw new NotFoundException("");
        }
    }

    public void removeLike(Long userId, Long filmId) {
        storage.removeLike(userId, filmId);
    }
}
