package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface DirectorRepository {
    Collection<Director> findAll();

    Optional<Director> findById(Long id);

    Director save(Director director);

    Director update(Director director);

    void delete(Long id);

    Collection<Director> findByIds(Set<Long> ids);
}
