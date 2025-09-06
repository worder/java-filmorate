package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmLikesService;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final FilmLikesService likesService;

    @GetMapping
    public Collection<FilmDto> getAll() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody NewFilmRequest film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long userId, @PathVariable Long filmId) {
        likesService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long userId, @PathVariable Long filmId) {
        likesService.removeLike(userId, filmId);
    }
}