package ru.yandex.practicum.filmorate.dal.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Primary @Repository("filmDbRepository")
public class FilmDbRepository extends BaseDbRepository<Film> implements FilmRepository {
    private static final String FIND_ALL_QUERY = "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name " +
            "FROM films f " +
            "JOIN mpa_ratings m ON m.id=f.mpa_rating_id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name "
            + "FROM films f "
            + "JOIN mpa_ratings m ON m.id=f.mpa_rating_id "
            + "WHERE f.id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films " +
            "(name, description, release_date, duration, mpa_rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?";

    public FilmDbRepository(JdbcTemplate db, RowMapper<Film> mapper) {
        super(db, mapper);
    }

    public Film save(Film film) {
        MpaRating mpa = film.getMpa();
        long id = this.insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                film.getDuration(),
                mpa != null ? mpa.getId() : null
        );
        return film.toBuilder().id(id).build();
    }

    public Film update(Film film) {
        MpaRating mpa = film.getMpa();
        this.update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                film.getDuration(),
                mpa != null ? mpa.getId() : null
        );
        return film;
    }

    public Optional<Film> findById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Film> findAll() {
        return this.findMany(FIND_ALL_QUERY);
    }
}
