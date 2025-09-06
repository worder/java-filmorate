package ru.yandex.practicum.filmorate.dto.film;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.Set;

@Value
@Builder
public class FilmDto {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Set<Genre> genres;
    MpaRating mpa;
}
