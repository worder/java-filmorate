package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class Film {
    Integer id;
    Set<Integer> likes;

    @NotBlank(message = "Film name should not be empty")
    String name;

    @Length(max = 200, message = "Description length should be less than 200 characters")
    String description;

    @FilmReleaseDate
    LocalDate releaseDate;

    @Positive(message = "Movie duration should be positive number")
    Integer duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || this.id == null) {
            return false;
        }

        Film film = (Film) o;
        return Objects.equals(id, film.id)
                && Objects.equals(likes, film.likes)
                && Objects.equals(name, film.name)
                && Objects.equals(description, film.description)
                && Objects.equals(releaseDate, film.releaseDate)
                && Objects.equals(duration, film.duration);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(likes);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(releaseDate);
        result = 31 * result + Objects.hashCode(duration);
        return result;
    }
}
