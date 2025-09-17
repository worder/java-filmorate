package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.model.Director;

public class DirectorMapper {
    public static DirectorDto mapToDirectorDto(Director director) {
        return DirectorDto.builder()
                .id(director.getId())
                .name(director.getName())
                .build();
    }

    public static Director mapToDirector(NewDirectorRequest request) {
        return Director.builder()
                .name(request.getName())
                .build();
    }

    public static Director updateDirectorFields(Director director, UpdateDirectorRequest request) {
        if (request.hasName()) {
            return director.toBuilder()
                    .name(request.getName())
                    .build();
        }
        return director;
    }
}
