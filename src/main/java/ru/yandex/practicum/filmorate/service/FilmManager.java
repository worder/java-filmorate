package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmManager {
    Film add(Film film);

    Optional<Film> get(Integer id);

    Film update(Film film);

    Collection<Film> getAll();
}
