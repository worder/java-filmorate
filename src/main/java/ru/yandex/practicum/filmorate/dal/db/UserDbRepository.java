package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Primary @Repository("userDbRepository")
public class UserDbRepository extends BaseDbRepository<User> implements UserRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users "
            + "SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    public UserDbRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> findAll() {
        return this.findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> findById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User save(User user) {
        long id =  this.insert(
                INSERT_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
        return user.toBuilder().id(id).build();
    }

    @Override
    public User update(User user) {
        this.update(
                UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE),
                user.getId()
        );
        return user;
    }
}
