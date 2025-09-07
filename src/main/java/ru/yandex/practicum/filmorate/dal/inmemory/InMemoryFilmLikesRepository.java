package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmLikesRepository;

import java.util.HashSet;

@Repository("inMemoryFilmLikesRepository")
@RequiredArgsConstructor
public class InMemoryFilmLikesRepository implements FilmLikesRepository {
    private final VolatileMemoryStorage storage;

    public void addLike(Long userId, Long filmId) {
        if (!storage.filmLikes.containsKey(filmId)) {
            storage.filmLikes.put(filmId, new HashSet<>());
        }
        storage.filmLikes.get(filmId).add(userId);
    }

    public void removeLike(Long userId, Long filmId) {
        if (storage.filmLikes.containsKey(filmId)) {
            storage.filmLikes.get(filmId).remove(userId);
        }
    }
}
