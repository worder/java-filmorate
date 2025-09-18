package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Optional;

public class BaseDbRepositoryMapper<T> extends BaseDbWrite {
    private final JdbcTemplate db;
    private final RowMapper<T> mapper;

    BaseDbRepositoryMapper(JdbcTemplate db, RowMapper<T> mapper) {
        super(db);
        this.db = db;
        this.mapper = mapper;
    }

    protected Optional<T> findOne(String query, Object... params) {
        try {
            return Optional.ofNullable(db.queryForObject(query, mapper, params));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return db.query(query, mapper, params);
    }

    protected List<T> findMany(String query, SqlParameterSource params) {
        NamedParameterJdbcTemplate nJdbc = new NamedParameterJdbcTemplate(this.db);
        return nJdbc.query(query, params, mapper);
    }
}
