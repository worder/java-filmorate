package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.dal.db.FilmDbRepository;
import ru.yandex.practicum.filmorate.dal.db.mapper.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Import({FilmDbRepository.class, FilmResultSetExtractor.class})
class FilmDbRepositoryDeleteIT {

    private final FilmDbRepository filmRepo;
    private final JdbcTemplate jdbc;


    @Autowired
    FilmDbRepositoryDeleteIT(FilmDbRepository filmRepo, JdbcTemplate jdbc) {
        this.filmRepo = filmRepo;
        this.jdbc = jdbc;
    }

    private long createFilm(String name, String description, LocalDate releaseDate, int duration) {
        var kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            var ps = conn.prepareStatement(
                    "INSERT INTO films (name, description, release_date, duration) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDate(3, java.sql.Date.valueOf(releaseDate));
            ps.setInt(4, duration);
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }


    private long createUser(String email, String login, String name, LocalDate birthday) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, email);
            ps.setString(2, login);
            ps.setString(3, name);
            ps.setDate(4, Date.valueOf(birthday));
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }


    private void addFilmGenre(long filmId, int genreId) {
        jdbc.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", filmId, genreId);
    }

    private void addLike(long userId, long filmId) {
        jdbc.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", userId, filmId);
    }

    private int countFilms(long id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM films WHERE id=?", Integer.class, id);
        return n == null ? 0 : n;
    }

    private int countFilmGenres(long filmId) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM film_genres WHERE film_id=?", Integer.class, filmId);
        return n == null ? 0 : n;
    }

    private int countFilmLikes(long filmId) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM film_likes WHERE film_id=?", Integer.class, filmId);
        return n == null ? 0 : n;
    }

    @Test
    void shouldDeleteFilm() {
        long f1 = createFilm("Film A", "desc A", LocalDate.of(2000, 1, 1), 100);
        long f2 = createFilm("Film B", "desc B", LocalDate.of(2001, 2, 2), 110);

        long u1 = createUser("a@example.com", "alice", "Alice", LocalDate.of(1990,1,1));
        long u2 = createUser("b@example.com", "bob",   "Bob",   LocalDate.of(1991,2,2));

        addFilmGenre(f1, 2);
        addLike(u1, f1);
        addLike(u2, f1);

        assertThat(countFilms(f1)).isEqualTo(1);
        assertThat(countFilmGenres(f1)).isEqualTo(1);
        assertThat(countFilmLikes(f1)).isEqualTo(2);

        filmRepo.deleteById(f1);

        assertThat(countFilms(f1)).isZero();
        assertThat(countFilmGenres(f1)).isZero();
        assertThat(countFilmLikes(f1)).isZero();

        assertThat(countFilms(f2)).isEqualTo(1);
    }

    @Test
    void shouldThrowInternalServerException() {
        assertThatThrownBy(() -> filmRepo.deleteById(999L)).isInstanceOf(InternalServerException.class);
    }
}

