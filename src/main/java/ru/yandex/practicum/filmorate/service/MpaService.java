package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository storage;

    public Collection<MpaRating> getAllMpa() {
        return storage.findAll();
    }

    public MpaRating getMpaById(Integer id) {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException("Mpa not found"));
    }

    public boolean isMpaExists(Integer id) {
        return storage.findById(id).isPresent();
    }
}
