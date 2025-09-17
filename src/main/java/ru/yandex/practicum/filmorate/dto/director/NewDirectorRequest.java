package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewDirectorRequest {
    @NotNull
    @NotBlank(message = "Director name should not be empty")
    private String name;
}
