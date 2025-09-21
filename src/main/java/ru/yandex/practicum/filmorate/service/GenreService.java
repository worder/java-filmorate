package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository storage;

    public Collection<Genre> getAllGenres() {
        return storage.findAll();
    }

    public Genre getGenreById(Integer id) {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found"));
    }

    public boolean isGenresExists(Set<Genre> genres) {
        Set<Integer> ids = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        return storage.findByIds(ids).size() == ids.size();
    }
}
