package ru.yandex.practicum.filmorate.dal.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component("inMemoryFilmRepository")
public class InMemoryFilmRepository implements FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();
    private int lastId = 0;

    @Override
    public Collection<Film> findAll() {
        return new ArrayList<>(this.films.values());
    }

    @Override
    public Film save(Film film) {
        long id = this.getNextId();
        Film addedFilm = film.toBuilder().id(id).build();
        this.films.put(id, addedFilm);
        return addedFilm;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film update(Film film) {
        if (!this.films.containsKey(film.getId())) {
            throw new InternalServerException("Failed to update film");
        }

        Film updatedFilm = film.toBuilder().build();
        this.films.put(film.getId(), updatedFilm);
        return updatedFilm;
    }

    @Override
    public Collection<Film> findPopular(int count) {
//        if (count <= 0) {
//            throw new InvalidArgumentException("Count should be greater than 0");
//        }
//
//        Comparator<Film> likesNumComparatorAsc = Comparator.comparingInt(f -> f.getLikes().size());
//        List<Film> sortedFilms = this.findAll().stream()
//                .sorted(likesNumComparatorAsc.reversed())
//                .toList();
//
//        return sortedFilms.subList(0, Math.min(count, sortedFilms.size()));

        return new HashSet<>();
    }

    private int getNextId() {
        return ++this.lastId;
    }
}
