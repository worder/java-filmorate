package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

public interface MpaRepository {
    Collection<MpaRating> findAll();

    Optional<MpaRating> findById(Integer id);
}
