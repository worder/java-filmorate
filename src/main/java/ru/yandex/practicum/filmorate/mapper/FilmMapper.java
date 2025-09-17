package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {
    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .genres(film.getGenres())
                .directors(film.getDirectors())
                .build();
    }

    public static Film mapToFilm(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .genres(request.getGenres())
                .directors(request.getDirectors())
                .mpa(request.getMpa())
                .build();
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        Film.FilmBuilder fb = film.toBuilder();
        if (request.hasName()) {
            fb.name(request.getName());
        }
        if (request.hasDescription()) {
            fb.description(request.getDescription());
        }
        if (request.hasReleaseDate()) {
            fb.releaseDate(request.getReleaseDate());
        }
        if (request.hasDuration()) {
            fb.duration(request.getDuration());
        }
        if (request.hasGenres()) {
            fb.genres(request.getGenres());
        }
        if (request.hasDirectors()) {
            fb.directors(request.getDirectors());
        }
        if (request.hasMpa()) {
            fb.mpa(request.getMpa());
        }
        return fb.build();
    }
}
