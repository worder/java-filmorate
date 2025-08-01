package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmManager;
import ru.yandex.practicum.filmorate.service.InMemoryFilmManager;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    FilmManager manager = new InMemoryFilmManager();

    @GetMapping
    public Collection<Film> getAll() {
        return manager.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        Film addedFilm = manager.add(film);
        log.debug("Created film with id: {}", addedFilm.getId());
        return addedFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (manager.get(film.getId()).isEmpty()) {
            log.warn("Try to update with unknown id: {}", film);
            throw new NotFoundException("Invalid id provided");
        }
        manager.update(film);
        log.debug("Updated film: {}", film);

        return film;
    }
}