package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Primary
@Repository("mpaDbRepository")
public class MpaDbRepository extends BaseDbRepository<MpaRating> implements MpaRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_ratings WHERE id = ?";

    public MpaDbRepository(JdbcTemplate db, RowMapper<MpaRating> mapper) {
        super(db, mapper);
    }

    @Override
    public List<MpaRating> findAll() {
        return this.findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<MpaRating> findById(Integer id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }
}
