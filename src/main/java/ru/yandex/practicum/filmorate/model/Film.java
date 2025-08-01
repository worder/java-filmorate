package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDate;

import java.time.LocalDate;

@Data
@Value
@Builder(toBuilder = true)
public class Film {
    Integer id;

    @NotBlank(message = "Film name should not be empty")
    String name;

    @Length(max = 200, message = "Description length should be less than 200 characters")
    String description;

    @FilmReleaseDate
    LocalDate releaseDate;

    @Positive(message = "Movie duration should be positive number")
    Integer duration;
}
