package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    Collection<Genre> findAll();

    Optional<Genre> findById(Integer id);

    Set<Genre> findByFilmId(Long id);
}
