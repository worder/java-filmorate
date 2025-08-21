package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film add(Film film);

    Film get(Integer id);

    Film update(Film film);

    Collection<Film> getAll();
}
