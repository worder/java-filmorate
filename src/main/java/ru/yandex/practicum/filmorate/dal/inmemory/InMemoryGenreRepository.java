package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("inMemoryGenreRepository")
@RequiredArgsConstructor
public class InMemoryGenreRepository implements GenreRepository {
    private final MemoryStorage storage;

    @Override
    public Collection<Genre> findAll() {
        return storage.genres.entrySet().stream().map(e ->
                Genre.builder()
                        .id(e.getKey())
                        .name(e.getValue())
                        .build()
        ).toList();
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        if (storage.genres.containsKey(id)) {
            return Optional.of(Genre.builder()
                    .id(id)
                    .name(storage.genres.get(id))
                    .build()
            );
        }
        return Optional.empty();
    }

    @Override
    public Set<Genre> findByFilmId(Long id) {
        if (storage.filmGenres.containsKey(id)) {
            return storage.filmGenres.get(id).stream()
                    .map(this::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

}
