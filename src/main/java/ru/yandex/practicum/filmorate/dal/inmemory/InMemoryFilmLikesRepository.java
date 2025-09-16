package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmLikesRepository;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.HashSet;
import java.util.List;

@Repository("inMemoryFilmLikesRepository")
@RequiredArgsConstructor
public class InMemoryFilmLikesRepository implements FilmLikesRepository {
    private final MemoryStorage storage;

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

    @Override
    public List<FilmLike> getAllLikes() {
        return List.of();
    }
}
