package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
@ToString
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}