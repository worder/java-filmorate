package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Set;

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

    public Set<Genre> getFilmGenres(Long filmId) {
        return storage.findByFilmId(filmId);
    }

    public boolean isGenreExists(Integer genreId) {
        return storage.findById(genreId).isPresent();
    }
}
