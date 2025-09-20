package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDate;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Value
public class NewFilmRequest {
    @NotBlank(message = "Film name should not be empty")
    String name;

    @Length(max = 200, message = "Description length should be less than 200 characters")
    String description;

    @NotNull
    @FilmReleaseDate
    LocalDate releaseDate;

    @NotNull
    @Positive(message = "Movie duration should be positive number")
    Integer duration;

    LinkedHashSet<Genre> genres;

    LinkedHashSet<Director> directors;

    MpaRating mpa;
}
