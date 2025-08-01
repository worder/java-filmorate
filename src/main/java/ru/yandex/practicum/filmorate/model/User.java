package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;

@Data
@Value
@Builder(toBuilder = true)
public class User {
    Integer id;

    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Login should not be empty")
    @Pattern(regexp = "^\\S+$", message = "Login should not contain whitespace characters")
    String login;

    String name;

    @PastOrPresent(message = "Birthday date should not be in future")
    LocalDate birthday;
}
