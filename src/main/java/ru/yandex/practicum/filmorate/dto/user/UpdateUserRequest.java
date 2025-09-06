package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@ToString
public class UpdateUserRequest {
    @NotNull
    Long id;

    @NotBlank(message = "Login should not be empty")
    @Pattern(regexp = "^\\S+$", message = "Login should not contain whitespace characters")
    String login;

    @Email(message = "Email should be valid")
    String email;

    @PastOrPresent(message = "Birthday date should not be in future")
    LocalDate birthday;

    String name;

    public boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasBirthday() {
        return !(birthday == null);
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }
}
