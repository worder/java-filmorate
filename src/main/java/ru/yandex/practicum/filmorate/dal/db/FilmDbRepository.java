package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Primary
@Repository("filmDbRepository")
public class FilmDbRepository extends BaseDbRepository<Film> implements FilmRepository {
    private static final String SELECT_FILM_WITH_MPA = "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name ";
    private static final String JOIN_MPA = "JOIN mpa_ratings m ON m.id=f.mpa_rating_id ";

    private static final String FIND_ALL_QUERY = """
            %s FROM films f %s
            """.formatted(SELECT_FILM_WITH_MPA, JOIN_MPA);

    private static final String FIND_BY_ID_QUERY = """
            %s FROM films f %s WHERE f.id = ?
            """.formatted(SELECT_FILM_WITH_MPA, JOIN_MPA);

    private static final String INSERT_FILM_QUERY = """
            INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    private static final String UPDATE_FILM_QUERY = """
            UPDATE films
            SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?
            """;

    private static final String FIND_POPULAR_QUERY = """
            %s, count(f.id) AS likes_count
            FROM films f
            %s
            JOIN film_likes fl ON f.id=fl.film_id
            GROUP BY f.id
            ORDER BY likes_count DESC
            LIMIT ?
            """.formatted(SELECT_FILM_WITH_MPA, JOIN_MPA);

    private final JdbcTemplate db;

    public FilmDbRepository(JdbcTemplate db, RowMapper<Film> mapper) {
        super(db, mapper);
        this.db = db;
    }

    @Override
    public Film save(Film film) {
        MpaRating mpa = film.getMpa();
        long newFilmId = this.insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                film.getDuration(),
                mpa != null ? mpa.getId() : null
        );

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre g : genres) {
                db.update(INSERT_FILM_GENRE_QUERY, newFilmId, g.getId());
            }
        }

        return film.toBuilder().id(newFilmId).build();
    }

    @Override
    public Film update(Film film) {
        MpaRating mpa = film.getMpa();
        this.update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                film.getDuration(),
                mpa != null ? mpa.getId() : null
        );

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            db.update(DELETE_FILM_GENRES_QUERY, film.getId());
            for (Genre g : genres) {
                db.update(INSERT_FILM_GENRE_QUERY, film.getId(), g.getId());
            }
        }

        return film;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> findAll() {
        return this.findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<Film> findPopular(int count) {
        return this.findMany(FIND_POPULAR_QUERY, count);
    }

    // Новый метод для получения списка популярных фильмов с фильтрами по жанру и году
    @Override
    public List<Film> findPopular(int count, Integer genreId, Integer year) {
        StringBuilder query = new StringBuilder(
                String.format("%s, COUNT(f.id) AS likes_count FROM films f %s", SELECT_FILM_WITH_MPA, JOIN_MPA)
        );

        List<Object> params = new ArrayList<>();

        StringBuilder whereClause = new StringBuilder();

        if (genreId != null) {
            query.append(" JOIN film_genres fg ON fg.film_id = f.id ");
            whereClause.append("WHERE fg.genre_id = ? ");
            params.add(genreId);
        }

        if (year != null) {
            if (genreId != null) {
                whereClause.append("AND ");
            } else {
                whereClause.append("WHERE ");
            }
            whereClause.append("EXTRACT(YEAR FROM f.release_date) = ? ");
            params.add(year);
        }

        // Добавляем JOIN film_likes перед whereClause, чтобы он был в секции FROM/JOIN, а не после WHERE
        query.append(" JOIN film_likes fl ON fl.film_id = f.id ");
        query.append(whereClause);
        query.append(" GROUP BY f.id ");
        query.append(" ORDER BY likes_count DESC ");
        query.append(" LIMIT ?");
        params.add(count);

        return this.findMany(query.toString(), params.toArray());
    }
}