package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFilmManager implements FilmManager {

    private final Map<Integer, Film> films = new HashMap<>();
    private int lastId = 0;

    public Film add(Film film) {
        int id = this.getNextId();
        Film addedFilm = film.toBuilder()
                .id(id)
                .build();

        this.films.put(id, addedFilm);
        return addedFilm;
    }

    public Optional<Film> get(Integer id) {
        return Optional.of(films.get(id));
    }

    public Film update(Film film) {
        this.films.put(film.getId(), film);
        return film;
    }

    public Collection<Film> getAll() {
        return this.films.values();
    }

    private int getNextId() {
        return ++this.lastId;
    }
}
