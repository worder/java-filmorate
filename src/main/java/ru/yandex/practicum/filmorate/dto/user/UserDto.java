package ru.yandex.practicum.filmorate.dto.user;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class UserDto {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
