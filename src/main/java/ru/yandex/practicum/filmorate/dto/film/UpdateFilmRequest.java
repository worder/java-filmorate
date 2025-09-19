package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.ToString;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDate;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Value
@ToString
public class UpdateFilmRequest {
    @NotNull
    Long id;

    @NotBlank(message = "Film name should not be empty")
    String name;

    @Length(max = 200, message = "Description length should be less than 200 characters")
    String description;

    @FilmReleaseDate
    LocalDate releaseDate;

    @Positive(message = "Movie duration should be positive number")
    Integer duration;

    LinkedHashSet<Genre> genres;

    LinkedHashSet<Director> directors;

    MpaRating mpa;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasGenres() {
        return genres != null;
    }

    public boolean hasDirectors() {
        return directors != null;
    }

    public boolean hasMpa() {
        return mpa != null;
    }
}
