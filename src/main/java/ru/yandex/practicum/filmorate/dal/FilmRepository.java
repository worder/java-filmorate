package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Collection<Film> findAll();

    Collection<Film> findPopular(int count);

    Optional<Film> findById(Long id);

    Film save(Film film);

    Film update(Film film);
}
