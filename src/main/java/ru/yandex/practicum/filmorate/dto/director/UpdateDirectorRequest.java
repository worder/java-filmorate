package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class UpdateDirectorRequest {
    @NotNull
    Long id;

    @NotBlank(message = "Director name should not be empty")
    String name;

    public boolean hasName() {
        return name != null;
    }
}
