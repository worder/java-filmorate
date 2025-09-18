package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.*;
import java.util.stream.Collectors;

@Repository("inMemoryFilmRepository")
@RequiredArgsConstructor
public class InMemoryFilmRepository implements FilmRepository {
    private final MemoryStorage storage;

    private int lastId = 0;

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(storage.films.values().stream()
                .map(this::buildFilm)
                .toList()
        );
    }

    @Override
    public Film save(Film film) {
        long id = this.getNextId();
        Film addedFilm = film.toBuilder()
                .id(id)
                .build();
        storage.films.put(id, addedFilm);

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            storage.filmGenres.put(id, new HashSet<>());
            for (Genre g : genres) {
                storage.filmGenres.get(id).add(g.getId());
            }
        }

        return this.buildFilm(addedFilm);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(this.buildFilm(storage.films.get(id)));
    }

    @Override
    public Film update(Film film) {
        if (!storage.films.containsKey(film.getId())) {
            throw new InternalServerException("Failed to update film");
        }

        Film updatedFilm = film.toBuilder().build();
        storage.films.put(film.getId(), updatedFilm);

        return this.buildFilm(updatedFilm);
    }

    @Override
    public List<Film> findRecommendations(long userId) {
        return List.of();
    }


    public List<Film> findPopular(int count) {
        return storage.filmLikes.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size())
                .map(Map.Entry::getKey)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::buildFilm)
                .limit(count)
                .toList();
    }

    @Override
    public List<Film> findPopular(int count, Integer genreId, Integer year) {
        return List.of();
    }

    private Film buildFilm(Film filmFromStorage) {
        if (filmFromStorage == null) {
            return null;
        }

        Film.FilmBuilder fb = filmFromStorage.toBuilder();
        if (storage.filmGenres.containsKey(filmFromStorage.getId())) {
            fb.genres(
                    storage.filmGenres.get(filmFromStorage.getId()).stream()
                            .map(id -> Genre.builder()
                                    .id(id)
                                    .name(storage.genres.get(id))
                                    .build())
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        if (filmFromStorage.getMpa() != null && storage.mpaRatings.containsKey(filmFromStorage.getMpa().getId())) {
            fb.mpa(MpaRating.builder()
                    .id(filmFromStorage.getMpa().getId())
                    .name(storage.mpaRatings.get(filmFromStorage.getMpa().getId()))
                    .build()
            );
        }

        return fb.build();
    }

    @Override
    public void deleteById(Long id) {
        storage.films.remove(id);
        storage.filmGenres.remove(id);
        storage.filmLikes.remove(id);
    }

    @Override
    public List<Film> findFilmsByDirectorIdSortByLikes(Long directorId) {
        return List.of();
    }

    @Override
    public List<Film> findFilmsByDirectorIdSortByYear(Long directorId) {
        return List.of();
    }

    private int getNextId() {
        return ++this.lastId;
    }
}
