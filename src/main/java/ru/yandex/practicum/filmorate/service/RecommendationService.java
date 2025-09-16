package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmLikesRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для предоставления рекомендаций фильмов пользователям на основе Slope One.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserService userService;
    private final FilmService filmService;
    private final FilmLikesRepository likesRepository;

    /**
     * Получает рекомендации фильмов для указанного пользователя.
     * @param userId ID пользователя
     * @return Список рекомендованных фильмов (топ-10 по скорингу)
     * @throws NotFoundException если пользователь не найден
     */
    public List<FilmDto> getRecommendations(Long userId) {
        // Проверяем существование пользователя
        validateUser(userId);

        // Получаем все лайки из базы
        List<FilmLike> allLikes = getAllLikes();

        // Строим карты для анализа лайков
        Map<Long, Set<Long>> userToFilms = buildUserToFilmsMap(allLikes);
        Map<Long, Set<Long>> filmToUsers = buildFilmToUsersMap(allLikes);

        // Получаем фильмы, которые пользователь уже лайкнул
        Set<Long> likedByUser = userToFilms.getOrDefault(userId, Collections.emptySet());

        // Получаем все фильмы
        Set<Long> allFilmIds = getAllFilmIds();

        // Определяем фильмы, которые пользователь еще не видел
        List<Long> unseenFilms = getUnseenFilms(allFilmIds, likedByUser);

        // Вычисляем скоринг для каждого непросмотренного фильма
        Map<Long, Integer> filmScores = calculateFilmScores(unseenFilms, likedByUser, filmToUsers);

        // Получаем топ-10 фильмов по скорингу
        List<Long> recommendedFilmIds = getTopRecommendedFilmIds(filmScores);

        // Преобразуем ID фильмов в DTO
        List<FilmDto> recommendations = getFilmDtos(recommendedFilmIds);

        log.info("Generated {} recommendations for user {}", recommendations.size(), userId);
        return recommendations;
    }

    /**
     * Проверяет существование пользователя.
     * @param userId ID пользователя
     * @throws NotFoundException если пользователь не найден
     */
    private void validateUser(Long userId) {
        if (!userService.userExists(userId)) {
            throw new NotFoundException("User not found");
        }
    }

    /**
     * Получает все лайки из базы данных.
     * @return Список всех лайков
     */
    private List<FilmLike> getAllLikes() {
        return likesRepository.getAllLikes();
    }

    /**
     * Строит карту, где ключ — ID пользователя, значение — набор ID лайкнутых фильмов.
     * @param allLikes Список всех лайков
     * @return Карта пользователь -> набор фильмов
     */
    private Map<Long, Set<Long>> buildUserToFilmsMap(List<FilmLike> allLikes) {
        return allLikes.stream()
                .collect(Collectors.groupingBy(FilmLike::getUserId,
                        Collectors.mapping(FilmLike::getFilmId, Collectors.toSet())));
    }

    /**
     * Строит карту, где ключ — ID фильма, значение — набор ID пользователей, лайкнувших фильм.
     * @param allLikes Список всех лайков
     * @return Карта фильм -> набор пользователей
     */
    private Map<Long, Set<Long>> buildFilmToUsersMap(List<FilmLike> allLikes) {
        return allLikes.stream()
                .collect(Collectors.groupingBy(FilmLike::getFilmId,
                        Collectors.mapping(FilmLike::getUserId, Collectors.toSet())));
    }

    /**
     * Получает набор ID всех фильмов.
     * @return Набор ID всех фильмов
     */
    private Set<Long> getAllFilmIds() {
        return filmService.getAllFilms().stream()
                .map(FilmDto::getId)
                .collect(Collectors.toSet());
    }

    /**
     * Определяет фильмы, которые пользователь еще не видел (не лайкнул).
     * @param allFilmIds Набор всех ID фильмов
     * @param likedByUser Набор ID фильмов, лайкнутых пользователем
     * @return Список ID непросмотренных фильмов
     */
    private List<Long> getUnseenFilms(Set<Long> allFilmIds, Set<Long> likedByUser) {
        return allFilmIds.stream()
                .filter(filmId -> !likedByUser.contains(filmId))
                .collect(Collectors.toList());
    }

    /**
     * Вычисляет скоринг для каждого непросмотренного фильма на основе совместных лайков.
     * Скоринг — сумма размеров пересечений пользователей, лайкнувших целевой фильм и фильмы пользователя.
     * @param unseenFilms Список ID фильмов, которые пользователь еще не лайкнул (непросмотренные).
     * @param likedByUser Набор ID фильмов, которые целевой пользователь уже лайкнул.
     * @param filmToUsers Карта, где ключ — ID фильма, а значение — набор ID пользователей, лайкнувших этот фильм.
     */
    private Map<Long, Integer> calculateFilmScores(
            List<Long> unseenFilms,
            Set<Long> likedByUser,
            Map<Long, Set<Long>> filmToUsers
    ) {
        Map<Long, Integer> filmScores = new HashMap<>();
        // Перебираем непросмотренные фильмы, которые пользователь еще не лайкнул
        for (Long j : unseenFilms) {
            //Для текущего непросмотренного фильма из filmToUsers извлекается множество
            // ID пользователей, которые его лайкнули.
            Set<Long> usersLikedJ = filmToUsers.getOrDefault(j, Collections.emptySet());
            int score = 0;
            // Перебираем все фильмы, которые пользователь уже лайкнул
            for (Long i : likedByUser) {
                Set<Long> usersLikedI = filmToUsers.getOrDefault(i, Collections.emptySet());
                // Для текущего лайкнутого фильма, целевым пользователем, из filmToUsers извлекается
                // множество ID пользователей, которые тоже его лайкнули и считается их количество.
                int card = (int) usersLikedI.stream().filter(usersLikedJ::contains).count();
                score += card;
            }
            if (score > 0) { // Учитываем только фильмы с ненулевым скорингом
                filmScores.put(j, score);
            }
        }
        return filmScores;
    }

    /**
     * Получает топ-10 ID фильмов с наивысшим скорингом.
     * @param filmScores Карта ID фильма -> его скоринг
     * @return Список ID рекомендованных фильмов (максимум 10)
     */
    private List<Long> getTopRecommendedFilmIds(Map<Long, Integer> filmScores) {
        return filmScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует список ID фильмов в список DTO.
     * @param recommendedFilmIds Список ID рекомендованных фильмов
     * @return Список FilmDto
     */
    private List<FilmDto> getFilmDtos(List<Long> recommendedFilmIds) {
        return recommendedFilmIds.stream()
                .map(filmService::getFilmById)
                .collect(Collectors.toList());
    }
}