package ru.yandex.practicum.filmorate.dal.db.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        LinkedHashMap<Long, Film> filmsById = new LinkedHashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            if (!filmsById.containsKey(id)) {
                Film film = Film.builder()
                        .id(id)
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .mpa(MpaRating.builder()
                                .id(rs.getInt("mpa_id"))
                                .name(rs.getString("mpa_name"))
                                .build())
                        .genres(new LinkedHashSet<>())
                        .directors(new LinkedHashSet<>())
                        .build();
                filmsById.put(id, film);
            }

            if (rs.getString("genre_id") != null) {
                Genre genre = Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre_name"))
                        .build();
                filmsById.get(id).getGenres().add(genre);
            }

            if (rs.getString("director_id") != null) {
                Director director = Director.builder()
                        .id(rs.getLong("director_id"))
                        .name(rs.getString("director_name"))
                        .build();
                filmsById.get(id).getDirectors().add(director);
            }
        }

        return new ArrayList<>(filmsById.values());
    }
}
