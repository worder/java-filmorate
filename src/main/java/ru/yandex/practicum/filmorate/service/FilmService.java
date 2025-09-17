package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository storage;
    private final GenreService genreService;
    private final MpaService mpaService;
    private final DirectorService directorService;

    public enum FilmsSorting {
        likes,
        year
    }

    public List<FilmDto> getAllFilms() {
        return storage.findAll().stream().map(FilmMapper::mapToFilmDto).toList();
    }

    // Новый метод для получения популярных фильмов с опциональными фильтрами по жанру и году
    public List<FilmDto> getPopularFilms(Integer count, Integer genreId, Integer year) {
        if (count <= 0) {
            throw new InvalidArgumentException("Count should be > 0");
        }

        return storage.findPopular(count, genreId, year).stream().map(FilmMapper::mapToFilmDto).toList();
    }

    public FilmDto getFilmById(Long id) {
        return storage.findById(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Film not found"));
    }

    public FilmDto createFilm(NewFilmRequest request) {
        Film newFilm = FilmMapper.mapToFilm(request);

        this.validateGenres(newFilm.getGenres());
        this.validateMpa(newFilm.getMpa());
        this.validateDirectors(newFilm.getDirectors());

        newFilm = storage.save(newFilm);
        log.info("Created film: {} from data: {}", newFilm, request);

        return FilmMapper.mapToFilmDto(newFilm);
    }

    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film updatedFilm = storage.findById(request.getId())
                .map(f -> FilmMapper.updateFilmFields(f, request))
                .orElseThrow(() -> new InternalServerException("Failed to update film, film not found"));

        this.validateGenres(updatedFilm.getGenres());
        this.validateMpa(updatedFilm.getMpa());
        this.validateDirectors(updatedFilm.getDirectors());

        updatedFilm = storage.update(updatedFilm);
        log.info("Updated film: {} from data: {}", updatedFilm, request);

        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public void deleteFilm(Long id) {
        if (!filmExists(id)) {
            throw new NotFoundException("Film deletion failed, film not found");
        }
        storage.deleteById(id);
        log.info("Deleted film id={}", id);
    }

    public boolean filmExists(Long id) {
        return this.storage.findById(id).isPresent();
    }

    public List<FilmDto> getFilmsByDirectorId(Long directorId, FilmsSorting sort) {
        List<Film> films = switch (sort) {
            case year -> storage.findFilmsByDirectorIdSortByYear(directorId);
            case likes -> storage.findFilmsByDirectorIdSortByLikes(directorId);
        };

        return films.stream().map(FilmMapper::mapToFilmDto).toList();
    }


    private void validateGenres(Set<Genre> genres) {
        if (genres != null && !genreService.isGenresExists(genres)) {
            throw new NotFoundException("Failed to create film, genre not found");
        }
    }

    private void validateDirectors(Set<Director> directors) {
        if (directors != null && !directorService.isDirectorsExists(directors)) {
            throw new NotFoundException("Failed to create film, director not found");
        }
    }

    private void validateMpa(MpaRating mpa) {
        if (mpa != null && !mpaService.isMpaExists(mpa.getId())) {
            throw new NotFoundException("Failed to create film, mpa not found");
        }
    }
}