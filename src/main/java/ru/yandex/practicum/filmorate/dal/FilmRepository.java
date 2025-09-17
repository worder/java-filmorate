package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Collection<Film> findAll();


    // Новый метод для поиска популярных фильмов с фильтрами по жанру и году
    List<Film> findPopular(int count, Integer genreId, Integer year);

    Optional<Film> findById(Long id);

    Film save(Film film);

    Film update(Film film);

    void deleteById(Long id);
}