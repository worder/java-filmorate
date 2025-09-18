package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<DirectorDto> getAll() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public DirectorDto getOne(@PathVariable Long id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public DirectorDto create(@Valid @RequestBody NewDirectorRequest request) {
        return directorService.createDirector(request);
    }

    @PutMapping
    public DirectorDto update(@Valid @RequestBody UpdateDirectorRequest request) {
        return directorService.updateDirector(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        directorService.deleteDirector(id);
    }
}
