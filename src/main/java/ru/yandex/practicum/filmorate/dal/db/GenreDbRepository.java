package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Primary
@Repository("genreDbRepository")
public class GenreDbRepository extends BaseDbRepository<Genre> implements GenreRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_BY_FILM_ID_QUERY =
            "SELECT g.id, g.name " +
                    "FROM film_genres fg " +
                    "JOIN genres g ON g.id=fg.genre_id " +
                    "WHERE fg.film_id = ?";

    public GenreDbRepository(JdbcTemplate db, RowMapper<Genre> mapper) {
        super(db, mapper);
    }

    @Override
    public List<Genre> findAll() {
        return this.findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Set<Genre> findByFilmId(Long id) {
        return new HashSet<>(this.findMany(FIND_BY_FILM_ID_QUERY, id));
    }
}
