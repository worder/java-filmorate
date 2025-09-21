package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.time.LocalDate;

@Value
public class NewUserRequest {
    @NotBlank(message = "Login should not be empty")
    @Pattern(regexp = "^\\S+$", message = "Login should not contain whitespace characters")
    String login;

    @NotNull
    @Email(message = "Email should be valid")
    String email;

    @NotNull
    @PastOrPresent(message = "Birthday date should not be in future")
    LocalDate birthday;

    String name;
}
