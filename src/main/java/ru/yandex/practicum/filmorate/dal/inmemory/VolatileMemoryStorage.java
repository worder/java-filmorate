package ru.yandex.practicum.filmorate.dal.inmemory;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class VolatileMemoryStorage {
    public final Map<Long, User> users = new HashMap<>();
    public final Map<Long, Film> films = new HashMap<>();

    public final Map<Integer, String> genres;
    public final Map<Integer, String> mpaRatings;

    public final Map<Long, Set<Integer>> filmGenres = new HashMap<>();
    public final Map<Long, Set<Long>> filmLikes = new HashMap<>();
    public final Map<Long, Set<Long>> userFriends = new HashMap<>();
    public final Map<Long, Set<Long>> userFriendsPending = new HashMap<>();

    public VolatileMemoryStorage() {
        this.genres = new LinkedHashMap<>();
        this.genres.put(1, "Комедия");
        this.genres.put(2, "Драма");
        this.genres.put(3, "Мультфильм");
        this.genres.put(4, "Триллер");
        this.genres.put(5, "Документальный");
        this.genres.put(6, "Боевик");

        this.mpaRatings = new LinkedHashMap<>();
        this.mpaRatings.put(1, "G");
        this.mpaRatings.put(2, "PG");
        this.mpaRatings.put(3, "PG-13");
        this.mpaRatings.put(4, "R");
        this.mpaRatings.put(5, "NC-17");
    }
}
