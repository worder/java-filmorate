package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Optional;

public class BaseDbRepositoryExtractor<T> extends BaseDbWrite {
    private final JdbcTemplate db;
    private final ResultSetExtractor<List<T>> extractor;

    BaseDbRepositoryExtractor(JdbcTemplate db, ResultSetExtractor<List<T>> extractor) {
        super(db);
        this.db = db;
        this.extractor = extractor;
    }

    protected Optional<T> findOne(String query, Object... params) {
        List<T> results = db.query(query, this.extractor, params);
        if (results != null && !results.isEmpty()) {
            return Optional.of(results.getFirst());
        }

        return Optional.empty();
    }

    protected List<T> findMany(String query, Object... params) {
        return db.query(query, extractor, params);
    }

    protected List<T> findMany(String query, SqlParameterSource params) {
        NamedParameterJdbcTemplate nJdbc = new NamedParameterJdbcTemplate(this.db);
        return nJdbc.query(query, params, extractor);
    }
}
