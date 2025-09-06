package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<FilmDto> getAll() {
        return service.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable Long id) {
        return service.getFilmById(id);
    }
//
//    @GetMapping("/popular")
//    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
//        return service.getPopularFilms(count);
//    }
//
    @PostMapping
    public FilmDto create(@Valid @RequestBody NewFilmRequest film) {
        return service.createFilm(film);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest film) {
        return service.updateFilm(film);
    }
//
//    @PutMapping("/{id}/like/{userId}")
//    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
//        return service.addLike(id, userId);
//    }
//
//    @DeleteMapping("/{id}/like/{userId}")
//    public Film removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
//        return service.removeLike(id,  userId);
//    }
}