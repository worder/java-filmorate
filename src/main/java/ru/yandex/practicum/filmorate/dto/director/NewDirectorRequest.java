package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class NewDirectorRequest {
    @NotBlank(message = "Director name should not be empty")
    String name;
}
