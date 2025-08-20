package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int lastId = 0;

    public Film add(Film film) {
        int id = this.getNextId();
        Film addedFilm = film.toBuilder()
                .id(id)
                .likes(new HashSet<>())
                .build();

        this.films.put(id, addedFilm);
        log.debug("Created film: {}", film);

        return addedFilm;
    }

    public Film get(Integer id) {
        if (!films.containsKey(id)) {
            log.debug("Trying to get non-existing film with id: " + id);
            throw new NotFoundException("Film not found: id" + id);
        }

        return films.get(id);
    }

    public Film update(Film film) {
        if (film == null) {
            throw new InvalidArgumentException("Film is null");
        }
        if (!this.films.containsKey(film.getId())) {
            log.warn("Trying to update non-existing film: {}", film);
            throw new NotFoundException("Film not found. id:" + film.getId());
        }

        Film currentFilm = this.films.get(film.getId());

        Set<Integer> likes = film.getLikes();
        if (likes == null) {
            likes = currentFilm.getLikes();
        }

        Film updatedFilm = film.toBuilder()
                .likes(likes)
                .build();

        this.films.put(film.getId(), updatedFilm);
        log.debug("Updated film: {} to {}", currentFilm, updatedFilm);

        return updatedFilm;
    }

    public Collection<Film> getAll() {
        return new ArrayList<>(this.films.values());
    }

    private int getNextId() {
        return ++this.lastId;
    }
}
