package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage storage;
    private final UserStorage userStorage;

    public Film addLike(Integer filmId, Integer userId) {
        Film film = storage.get(filmId);
        if (!userStorage.hasUser(userId)) {
            throw new NotFoundException("User not found, id:" + userId);
        }

        Set<Integer> updatedLikes = new HashSet<>(film.getLikes());
        updatedLikes.add(userId);

        return storage.update(film.toBuilder().likes(updatedLikes).build());
    }

    public Film removeLike(Integer filmId, Integer userId) {
        Film film = storage.get(filmId);

        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("No like found for user id: " + userId);
        }

        Set<Integer> updatedLikes = new HashSet<>(film.getLikes());
        updatedLikes.remove(userId);

        return storage.update(film.toBuilder().likes(updatedLikes).build());
    }

    public Collection<Film> getPopularFilms(Integer count) {
        if (count <= 0) {
            throw new InvalidArgumentException("Count should be greater than 0");
        }

        Comparator<Film> likesNumComparatorAsc = Comparator.comparingInt(f -> f.getLikes().size());
        List<Film> sortedFilms = storage.getAll().stream()
                .sorted(likesNumComparatorAsc.reversed())
                .toList();

        return sortedFilms.subList(0, Math.min(count, sortedFilms.size()));
    }

}
