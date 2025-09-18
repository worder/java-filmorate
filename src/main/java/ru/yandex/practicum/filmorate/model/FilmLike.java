package ru.yandex.practicum.filmorate.model;

import lombok.Value;

// Модель для хранения пары пользователь-фильм для лайков
@Value
public class FilmLike {
    Long userId;
    Long filmId;
}