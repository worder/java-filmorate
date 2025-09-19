package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Primary
@Repository("filmDbRepository")
public class FilmDbRepository extends BaseDbRepositoryExtractor<Film> implements FilmRepository {

    // fetch all data with one request; %s can be film table name or subquery
    private static final String SELECT_FILMS_TEMPLATE = """
            SELECT f.*,
            	m.id AS mpa_id,
            	m.name AS mpa_name,
            	g.id AS genre_id,
            	g.name AS genre_name,
            	d.id AS director_id,
            	d.name AS director_name
            FROM %s f
            LEFT JOIN mpa_ratings m ON m.id = f.mpa_rating_id
            LEFT JOIN film_genres fg ON fg.film_id = f.id
            LEFT JOIN genres g ON g.id = fg.genre_id
            LEFT JOIN film_directors fd ON fd.film_id = f.id
            LEFT JOIN directors d ON d.id = fd.director_id
            """;

    private static final String FIND_ALL_QUERY = SELECT_FILMS_TEMPLATE.formatted("films");

    private static final String FIND_BY_ID_QUERY = """
            %s
            WHERE f.id = ?
            """.formatted(FIND_ALL_QUERY);

    private static final String INSERT_FILM_QUERY = """
            INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    private static final String INSERT_FILM_DIRECTOR_QUERY = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";

    private static final String DELETE_FILM_DIRECTORS_QUERY = "DELETE FROM film_directors WHERE film_id = ?";

    private static final String UPDATE_FILM_QUERY = """
            UPDATE films
            SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?
            WHERE id = ?
            """;

    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE id = ?";

    private static final String FIND_DIRECTOR_FILMS_SORT_BY_DATE_SUBQUERY = """
            (SELECT f.*
            FROM films f
            JOIN film_directors fd ON fd.film_id=f.id
            WHERE fd.director_id = ?
            ORDER BY EXTRACT(YEAR FROM release_date))
            """;

    private static final String FIND_DIRECTOR_FILMS_SORT_BY_LIKES_SUBQUERY = """
            (SELECT f.*, count(fl.film_id) AS likes_count
            FROM films f
            JOIN film_directors fd ON fd.film_id=f.id
            LEFT JOIN film_likes fl ON fl.film_id=f.id
            WHERE fd.director_id = ?
            GROUP BY f.id
            ORDER BY likes_count DESC)
            """;

    private static final String FIND_RECOMMENDED_FILMS_SUBQUERY = """
            (SELECT f.*, COUNT(DISTINCT fl2.user_id) as score
            FROM film_likes fl1
            JOIN film_likes fl2 ON fl1.user_id = fl2.user_id
            JOIN films f ON fl2.film_id = f.id
            WHERE fl1.user_id != ?
              AND fl1.film_id IN (SELECT film_id FROM film_likes WHERE user_id = ?)
              AND fl2.film_id NOT IN (SELECT film_id FROM film_likes WHERE user_id = ?)
            GROUP BY fl2.film_id
            ORDER BY score DESC)
            """;

    private static final String FIND_COMMON_FILMS = SELECT_FILMS_TEMPLATE.formatted("""
            (SELECT f.id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   f.mpa_rating_id,
                   COUNT(flall.user_id) as total_reviews
            FROM films f
                     JOIN film_likes fl1 ON fl1.film_id = f.id
                     JOIN film_likes fl2 ON fl2.film_id = f.id
                     JOIN film_likes flall ON f.id = flall.film_id
            WHERE fl1.user_id = ?
              AND fl2.user_id = ?
            GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa_rating_id
            ORDER BY total_reviews DESC)
            """);

    private final JdbcTemplate db;

    public FilmDbRepository(JdbcTemplate db, ResultSetExtractor<List<Film>> extractor) {
        super(db, extractor);
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
            genres.forEach(g -> db.update(INSERT_FILM_GENRE_QUERY, newFilmId, g.getId()));
        }

        Set<Director> directors = film.getDirectors();
        if (directors != null) {
            directors.forEach(d -> db.update(INSERT_FILM_DIRECTOR_QUERY, newFilmId, d.getId()));
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
                mpa != null ? mpa.getId() : null,
                film.getId()
        );

        Set<Genre> genres = film.getGenres();
        db.update(DELETE_FILM_GENRES_QUERY, film.getId());
        if (genres != null) {
            genres.forEach(g -> db.update(INSERT_FILM_GENRE_QUERY, film.getId(), g.getId()));
        }

        Set<Director> directors = film.getDirectors();
        db.update(DELETE_FILM_DIRECTORS_QUERY, film.getId());
        if (directors != null) {
            directors.forEach(d -> db.update(INSERT_FILM_DIRECTOR_QUERY, film.getId(), d.getId()));
        }

        return film;
    }

    @Override
    public List<Film> findRecommendations(long userId) {
        return this.findMany(SELECT_FILMS_TEMPLATE.formatted(FIND_RECOMMENDED_FILMS_SUBQUERY), userId, userId, userId);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> findAll() {
        return this.findMany(FIND_ALL_QUERY);
    }


    // Новый метод для получения списка популярных фильмов с фильтрами по жанру и году
    @Override
    public List<Film> findPopular(int count, Integer genreId, Integer year) {
        StringBuilder query = new StringBuilder(
                "(SELECT f.*, COUNT(fl.user_id) AS likes_count FROM films f "
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

        // Добавляем LEFT JOIN film_likes перед whereClause, чтобы он был в секции FROM/JOIN, а не после WHERE
        query.append("LEFT JOIN film_likes fl ON fl.film_id = f.id ");
        query.append(whereClause);
        query.append(" GROUP BY f.id ");
        query.append(" ORDER BY likes_count DESC, f.id ASC");
        query.append(" LIMIT ?)");
        params.add(count);

        return this.findMany(SELECT_FILMS_TEMPLATE.formatted(query.toString()), params.toArray());
    }

    @Override
    public List<Film> findFilmsByDirectorIdSortByYear(Long directorId) {
        return this.findMany(SELECT_FILMS_TEMPLATE
                .formatted(FIND_DIRECTOR_FILMS_SORT_BY_DATE_SUBQUERY), directorId);
    }

    @Override
    public List<Film> findFilmsByDirectorIdSortByLikes(Long directorId) {
        return this.findMany(SELECT_FILMS_TEMPLATE
                .formatted(FIND_DIRECTOR_FILMS_SORT_BY_LIKES_SUBQUERY), directorId);
    }

    @Override
    public void deleteById(Long id) {
        this.update(DELETE_FILM_QUERY, id);
    }

    public List<Film> search(String query, Set<String> by) {
        boolean byTitle = by.contains("title");
        boolean byDirector = by.contains("director");

        if (!byTitle && !byDirector) byTitle = true;

        String like = "%" + escapeLike(query.toLowerCase(Locale.ROOT)) + "%";
        StringBuilder sql = new StringBuilder("(SELECT f.*, COALESCE(COUNT(fl.user_id), 0) AS likes_count FROM films f ");

        sql.append("LEFT JOIN film_likes fl ON fl.film_id = f.id ");

        if (byDirector) {
            sql.append("LEFT JOIN film_directors fd ON fd.film_id = f.id ");
            sql.append("LEFT JOIN directors d ON d.id = fd.director_id ");
        }

        List<String> predicates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (byTitle) {
            predicates.add("LOWER(f.name) LIKE ? ESCAPE '\\'");
            params.add(like);
        }
        if (byDirector) {
            predicates.add("LOWER(d.name) LIKE ? ESCAPE '\\'");
            params.add(like);
        }

        sql.append("WHERE ").append(String.join(" OR ", predicates)).append(" ");

        sql.append("GROUP BY f.id ");
        sql.append("ORDER BY likes_count DESC, f.id ASC ");
        sql.append(")");

        return this.findMany(SELECT_FILMS_TEMPLATE.formatted(sql.toString()), params.toArray());
    }

    private static String escapeLike(String s) {
        if (s == null) return "";
        return s
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    @Override
    public List<Film> findCommonFilms(Long userId, Long friendId) {
        return this.findMany(FIND_COMMON_FILMS, userId, friendId);
    }
}