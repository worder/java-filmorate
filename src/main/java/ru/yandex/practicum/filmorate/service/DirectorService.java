package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorRepository storage;

    public Collection<DirectorDto> getAllDirectors() {
        return storage.findAll().stream()
                .map(DirectorMapper::mapToDirectorDto)
                .toList();
    }

    public DirectorDto getDirectorById(Long id) {
        return storage.findById(id)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> new NotFoundException("Director not found"));
    }

    public DirectorDto createDirector(NewDirectorRequest request) {
        Director newDirector = storage.save(DirectorMapper.mapToDirector(request));
        log.info("Created director: {} from data: {}", newDirector, request);
        return DirectorMapper.mapToDirectorDto(newDirector);
    }

    public DirectorDto updateDirector(UpdateDirectorRequest request) {
        Director director = storage.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Director not found"));

        Director updatedDirector = DirectorMapper.updateDirectorFields(director, request);
        updatedDirector = storage.update(updatedDirector);

        log.info("Updated director: {} from data: {}", director, updatedDirector);
        return DirectorMapper.mapToDirectorDto(updatedDirector);
    }

    public void deleteDirector(long id) {
        if (!this.isExists(id)) {
            throw new NotFoundException("Director not found");
        }
        storage.delete(id);
        log.info("Deleted director id: {}", id);
    }

    public boolean isExists(Long id) {
        return storage.findById(id).isPresent();
    }
}
