package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

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
                .build();
    }

    public static Film mapToFilm(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .genres(request.getGenres())
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
        if (request.hasMpa()) {
            fb.mpa(request.getMpa());
        }
        return fb.build();
    }
//
//    public static User updateUserFields(User user, UpdateUserRequest request) {
//        User.UserBuilder userBuilder = user.toBuilder();
//        if (request.hasEmail()) {
//            userBuilder.email(request.getEmail());
//        }
//        if (request.hasLogin()) {
//            userBuilder.login(request.getLogin());
//        }
//        if (request.hasName()) {
//            userBuilder.name(request.getName());
//        }
//        if (request.hasBirthday()) {
//            userBuilder.birthday(request.getBirthday());
//        }
//        return userBuilder.build();
//    }
}
