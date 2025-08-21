package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || this.id == null) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(friends, user.friends)
                && Objects.equals(email, user.email)
                && Objects.equals(login, user.login)
                && Objects.equals(name, user.name)
                && Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(friends);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(login);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(birthday);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", friends=" + friends +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
