package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository("directorDbRepository")
public class DirectorDbRepository extends BaseDbRepositoryMapper<Director> implements DirectorRepository {

    private static final String FIND_ALL_QUERY = "SELECT * FROM directors";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM directors WHERE id = ?";

    private static final String FIND_BY_IDS_QUERY = "SELECT * FROM directors WHERE id IN (:ids)";

    private static final String INSERT_QUERY = "INSERT INTO directors (name) VALUES (?)";

    private static final String UPDATE_QUERY = "UPDATE directors SET name = ? WHERE id = ?";

    private static final String DELETE_QUERY = "DELETE FROM directors WHERE id = ?";

    public DirectorDbRepository(JdbcTemplate db, RowMapper<Director> mapper) {
        super(db, mapper);
    }

    public Collection<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Director> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Director save(Director director) {
        Long id = this.insert(INSERT_QUERY, director.getName());
        return director.toBuilder()
                .id(id)
                .build();
    }

    public Director update(Director director) {
        this.insert(UPDATE_QUERY, director.getName(), director.getId());
        return director;
    }

    public void delete(Long id) {
        this.update(DELETE_QUERY, id);
    }

    public Set<Director> findByIds(Set<Long> ids) {
        return new HashSet<>(this.findMany(FIND_BY_IDS_QUERY, new MapSqlParameterSource("ids", ids)));
    }
}
