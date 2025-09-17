package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

/**
 * Сервис для предоставления рекомендаций фильмов пользователям на основе Slope One.
 * Алгоритм использует SQL-запрос для вычисления скоринга на основе совместных лайков.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserService userService;
    private final FilmService filmService;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Получает рекомендации фильмов для указанного пользователя.
     *
     * @param userId ID пользователя
     * @return Список рекомендованных фильмов (топ-10 по скорингу)
     * @throws NotFoundException если пользователь не найден
     */
    public List<FilmDto> getRecommendations(Long userId) {
        // Проверяем существование пользователя
        validateUser(userId);

        // SQL-запрос для вычисления скоринга непросмотренных фильмов
        String sql = """
                SELECT fl2.film_id, COUNT(DISTINCT fl2.user_id) as score
                FROM film_likes fl1
                JOIN film_likes fl2 ON fl1.user_id = fl2.user_id
                WHERE fl1.user_id != ?
                  AND fl1.film_id IN (SELECT film_id FROM film_likes WHERE user_id = ?)
                  AND fl2.film_id NOT IN (SELECT film_id FROM film_likes WHERE user_id = ?)
                GROUP BY fl2.film_id
                ORDER BY score DESC
                LIMIT 10;
                """;

        // Выполняем запрос, передавая userId трижды
        List<Long> recommendedFilmIds = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id"), userId, userId, userId);

        // Преобразуем ID фильмов в DTO
        List<FilmDto> recommendations = recommendedFilmIds.stream().map(filmService::getFilmById).toList();

        log.info("Generated {} recommendations for user {}", recommendations.size(), userId);
        return recommendations;
    }

    /**
     * Проверяет существование пользователя.
     *
     * @param userId ID пользователя
     * @throws NotFoundException если пользователь не найден
     */
    private void validateUser(Long userId) {
        if (!userService.userExists(userId)) {
            throw new NotFoundException("User not found");
        }
    }
}