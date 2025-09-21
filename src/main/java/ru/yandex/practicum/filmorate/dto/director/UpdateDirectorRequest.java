package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UpdateDirectorRequest {
    @NotNull
    Long id;

    @NotBlank(message = "Director name should not be empty")
    String name;

    public boolean hasName() {
        return name != null;
    }
}
