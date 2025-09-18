package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для проверки функциональности рекомендаций.
 * Проверяет корректность формирования рекомендаций фильмов на основе SQL-реализации Slope One.
 * Используется встроенная база данных H2, с 7 пользователями для тестирования.
 */
@SpringBootTest
@AutoConfigureTestDatabase
public class RecommendationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FilmService recommendationService;

    /**
     * Настройка тестовых данных перед каждым тестом.
     * Очищает таблицы и заполняет их 7 пользователями, 4 фильмами и лайками.
     */
    @BeforeEach
    public void setUp() {
        // Очистка таблиц
        jdbcTemplate.update("DELETE FROM film_likes");
        jdbcTemplate.update("DELETE FROM film_genres");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM genres");
        jdbcTemplate.update("DELETE FROM mpa_ratings");
        jdbcTemplate.update("DELETE FROM user_friends");
        jdbcTemplate.update("DELETE FROM users");

        // Добавление MPA-рейтингов
        jdbcTemplate.update("INSERT INTO mpa_ratings (id, name) VALUES (?, ?)", 1, "G");

        // Добавление 7 пользователей
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 1, "user1@example.com", "user1", "User1", LocalDate.of(1990, 1, 1));
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 2, "user2@example.com", "user2", "User2", LocalDate.of(1991, 2, 2));
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 3, "user3@example.com", "user3", "User3", LocalDate.of(1992, 3, 3));
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 4, "user4@example.com", "user4", "User4", LocalDate.of(1993, 4, 4));
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 5, "user5@example.com", "user5", "User5", LocalDate.of(1994, 5, 5));
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 6, "user6@example.com", "user6", "User6", LocalDate.of(1995, 6, 6));
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 7, "user7@example.com", "user7", "User7", LocalDate.of(1996, 7, 7));

        // Добавление 4 фильмов
        jdbcTemplate.update("INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)", 1, "Film1", "Description1", LocalDate.of(2000, 1, 1), 120, 1);
        jdbcTemplate.update("INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)", 2, "Film2", "Description2", LocalDate.of(2000, 2, 1), 130, 1);
        jdbcTemplate.update("INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)", 3, "Film3", "Description3", LocalDate.of(2000, 3, 1), 140, 1);
        jdbcTemplate.update("INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)", 4, "Film4", "Description4", LocalDate.of(2000, 4, 1), 150, 1);

        // Добавление лайков
        // User1: Film1, Film2
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 1, 1);
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 1, 2);
        // User2: Film1, Film3
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 2, 1);
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 2, 3);
        // User3: Film2, Film3
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 3, 2);
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 3, 3);
        // User4: Film1, Film2, Film3
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 4, 1);
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 4, 2);
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 4, 3);
        // User5: Film3
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 5, 3);
        // User6: Film1, Film4
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 6, 1);
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 6, 4);
        // User7: Film2, Film4
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 7, 2);
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", 7, 4);
    }

    /**
     * Проверяет рекомендации для пользователя с лайками, где есть пересечения с другими пользователями.
     * Для User1 (лайкнул Film1, Film2) ожидается рекомендация Film3 (score=3) и Film4 (score=2).
     */
    @Test
    public void testGetRecommendationsWithLikes() {
        List<FilmDto> recommendations = recommendationService.getRecommendations(1L);

        assertEquals(2, recommendations.size(), "Должно быть две рекомендации");
        assertEquals("Film3", recommendations.get(0).getName(), "Film3 должен быть первым (score=3)");
        assertEquals("Film4", recommendations.get(1).getName(), "Film4 должен быть вторым (score=2)");
    }

    /**
     * Проверяет рекомендации для пользователя без лайков.
     * Ожидается пустой список рекомендаций.
     */
    @Test
    public void testGetRecommendationsNoLikes() {
        // Добавляем пользователя без лайков
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)", 8, "user8@example.com", "user8", "User8", LocalDate.of(1997, 8, 8));

        List<FilmDto> recommendations = recommendationService.getRecommendations(8L);

        assertTrue(recommendations.isEmpty(), "Рекомендации должны быть пустыми для пользователя без лайков");
    }

    /**
     * Проверяет поведение при несуществующем пользователе.
     * Ожидается исключение NotFoundException.
     */
    @Test
    public void testGetRecommendationsUserNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> recommendationService.getRecommendations(999L), "Должно выброситься исключение NotFoundException");
        assertEquals("User not found", exception.getMessage(), "Сообщение об ошибке должно быть корректным");
    }

    /**
     * Проверяет корректность ранжирования рекомендаций.
     * Для User5 (лайкнул Film3) ожидается Film1 (score=3) и Film2 (score=3) в рекомендациях.
     */
    @Test
    public void testGetRecommendationsRanking() {
        List<FilmDto> recommendations = recommendationService.getRecommendations(5L);

        assertEquals(2, recommendations.size(), "Должно быть две рекомендации");
        assertTrue(recommendations.stream().anyMatch(f -> f.getName().equals("Film1")), "Film1 должен быть в рекомендациях (score=3)");
        assertTrue(recommendations.stream().anyMatch(f -> f.getName().equals("Film2")), "Film2 должен быть в рекомендациях (score=3)");
    }

    /**
     * Проверяет, что фильмы, лайкнутые пользователем, не включаются в рекомендации.
     * Для User1 (лайкнул Film1, Film2) проверяем, что они не в рекомендациях.
     */
    @Test
    public void testGetRecommendationsExcludesLikedFilms() {
        List<FilmDto> recommendations = recommendationService.getRecommendations(1L);

        assertFalse(recommendations.stream().anyMatch(f -> f.getName().equals("Film1")), "Film1 не должен быть в рекомендациях");
        assertFalse(recommendations.stream().anyMatch(f -> f.getName().equals("Film2")), "Film2 не должен быть в рекомендациях");
    }

    /**
     * Проверяет рекомендации для пользователя с частичным пересечением.
     * Для User6 (лайкнул Film1, Film4) ожидается Film3 (score=2) и Film2 (score=2).
     */
    @Test
    public void testGetRecommendationsPartialOverlap() {
        List<FilmDto> recommendations = recommendationService.getRecommendations(6L);

        assertEquals(2, recommendations.size(), "Должно быть две рекомендации");
        assertTrue(recommendations.stream().anyMatch(f -> f.getName().equals("Film3")), "Film3 должен быть в рекомендациях (score=2)");
        assertTrue(recommendations.stream().anyMatch(f -> f.getName().equals("Film2")), "Film2 должен быть в рекомендациях (score=2)");
    }
}