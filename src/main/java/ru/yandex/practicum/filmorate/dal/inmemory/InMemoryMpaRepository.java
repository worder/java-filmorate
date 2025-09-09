package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

@Repository("inMemoryMpaRepository")
@RequiredArgsConstructor
public class InMemoryMpaRepository implements MpaRepository {
    private final MemoryStorage storage;

    @Override
    public Collection<MpaRating> findAll() {
        return storage.mpaRatings.entrySet().stream().map(e ->
                MpaRating.builder()
                        .id(e.getKey())
                        .name(e.getValue())
                        .build()
        ).toList();
    }

    @Override
    public Optional<MpaRating> findById(Integer id) {
        if (storage.mpaRatings.containsKey(id)) {
            return Optional.of(MpaRating.builder()
                    .id(id)
                    .name(storage.mpaRatings.get(id))
                    .build()
            );
        }
        return Optional.empty();
    }
}
