package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;

@Data
@Value
@Builder(toBuilder = true)
public class User {
    Integer id;
    Set<Integer> friends;

    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Login should not be empty")
    @Pattern(regexp = "^\\S+$", message = "Login should not contain whitespace characters")
    String login;

    String name;

    @PastOrPresent(message = "Birthday date should not be in future")
    LocalDate birthday;
}
