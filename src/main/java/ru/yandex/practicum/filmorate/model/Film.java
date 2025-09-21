package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Value
@Builder(toBuilder = true)
@ToString
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    LinkedHashSet<Genre> genres;
    LinkedHashSet<Director> directors;
    MpaRating mpa;
}
