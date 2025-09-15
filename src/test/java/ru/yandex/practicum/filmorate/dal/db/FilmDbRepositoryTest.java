package ru.yandex.practicum.filmorate.dal.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmDbRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FilmDbRepository filmDbRepository;

    @BeforeEach
    public void setUp() {
        // Очистка таблиц перед каждым тестом
        jdbcTemplate.update("DELETE FROM film_likes");
        jdbcTemplate.update("DELETE FROM film_genres");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM genres");
        jdbcTemplate.update("DELETE FROM mpa_ratings");
        jdbcTemplate.update("DELETE FROM user_friends");
        jdbcTemplate.update("DELETE FROM users");

        // Инициализация жанров
        jdbcTemplate.update("INSERT INTO genres (id, name) VALUES (?, ?)", 1, "Комедия");
        jdbcTemplate.update("INSERT INTO genres (id, name) VALUES (?, ?)", 2, "Драма");

        // Инициализация MPA-рейтингов
        jdbcTemplate.update("INSERT INTO mpa_ratings (id, name) VALUES (?, ?)", 1, "G");
        jdbcTemplate.update("INSERT INTO mpa_ratings (id, name) VALUES (?, ?)", 2, "PG");

        // Инициализация пользователей
        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)",
                1, "user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1)
        );
        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)",
                2, "user2@example.com", "user2", "User Two", LocalDate.of(1991, 2, 2)
        );
        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)",
                3, "user3@example.com", "user3", "User Three", LocalDate.of(1992, 3, 3)
        );

        // Добавление тестовых фильмов
        jdbcTemplate.update(
                "INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)",
                1, "Film1", "Description1", LocalDate.of(1960, 1, 1), 120, 1
        );
        jdbcTemplate.update(
                "INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)",
                2, "Film2", "Description2", LocalDate.of(1960, 2, 1), 130, 2
        );
        jdbcTemplate.update(
                "INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)",
                3, "Film3", "Description3", LocalDate.of(1970, 1, 1), 140, 1
        );

        // Привязка жанров к фильмам
        jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", 1, 1); // Film1 - Комедия
        jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", 2, 1); // Film2 - Комедия
        jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", 3, 2); // Film3 - Драма

        // Добавление лайков
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 1, 1); // Film1 - 1 лайк
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 2, 1); // Film1 - 2 лайка
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 1, 2); // Film2 - 1 лайк
    }

    // Тест 1: Проверка получения популярных фильмов без фильтров
    @Test
    public void testFindPopularWithoutFilters() {
        List<Film> films = filmDbRepository.findPopular(2, null, null);

        assertEquals(2, films.size(), "Должно вернуться 2 фильма");
        assertEquals("Film1", films.get(0).getName(), "Первый фильм должен быть Film1 (2 лайка)");
        assertEquals("Film2", films.get(1).getName(), "Второй фильм должен быть Film2 (1 лайк)");
    }

    // Тест 2: Проверка фильтрации по жанру (Комедия)
    @Test
    public void testFindPopularByGenre() {
        List<Film> films = filmDbRepository.findPopular(2, 1, null);

        assertEquals(2, films.size(), "Должно вернуться 2 фильма жанра Комедия");
        assertEquals("Film1", films.get(0).getName(), "Первый фильм должен быть Film1");
        assertEquals("Film2", films.get(1).getName(), "Второй фильм должен быть Film2");
    }

    // Тест 3: Проверка фильтрации по году (1960)
    @Test
    public void testFindPopularByYear() {
        List<Film> films = filmDbRepository.findPopular(2, null, 1960);

        assertEquals(2, films.size(), "Должно вернуться 2 фильма за 1960 год");
        assertEquals("Film1", films.get(0).getName(), "Первый фильм должен быть Film1");
        assertEquals("Film2", films.get(1).getName(), "Второй фильм должен быть Film2");
    }

    // Тест 4: Проверка фильтрации по жанру и году (Комедия, 1960)
    @Test
    public void testFindPopularByGenreAndYear() {
        List<Film> films = filmDbRepository.findPopular(2, 1, 1960);

        assertEquals(2, films.size(), "Должно вернуться 2 фильма жанра Комедия за 1960 год");
        assertEquals("Film1", films.get(0).getName(), "Первый фильм должен быть Film1");
        assertEquals("Film2", films.get(1).getName(), "Второй фильм должен быть Film2");
    }

    // Тест 5: Проверка случая, когда фильмы не найдены (например, жанр Драма в 1960 году)
    @Test
    public void testFindPopularNoResults() {
        List<Film> films = filmDbRepository.findPopular(2, 2, 1960);

        assertEquals(0, films.size(), "Не должно быть фильмов жанра Драма за 1960 год");
    }

    // Тест 6: Проверка корректности сортировки по количеству лайков
    @Test
    public void testFindPopularOrderByLikes() {
        // Добавляем дополнительный лайк для Film2
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 3, 2); // Film2 - 2 лайка

        List<Film> films = filmDbRepository.findPopular(2, null, null);

        assertEquals(2, films.size(), "Должно вернуться 2 фильма");
        assertEquals("Film1", films.get(0).getName(), "Первый фильм должен быть Film1 (2 лайка)");
        assertEquals("Film2", films.get(1).getName(), "Второй фильм должен быть Film2 (2 лайка)");
    }
}