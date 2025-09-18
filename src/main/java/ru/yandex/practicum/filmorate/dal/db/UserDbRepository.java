package ru.yandex.practicum.filmorate.dal.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Primary
@Repository("userDbRepository")
public class UserDbRepository extends BaseDbRepositoryMapper<User> implements UserRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";

    private static final String FIND_USER_FRIENDS_QUERY = """
            (SELECT u.* FROM user_friends f JOIN users u ON f.friend_id=u.id WHERE f.user_id=?)
            UNION
            (SELECT u.* FROM user_friends f JOIN users u ON f.user_id=u.id WHERE f.friend_id=? AND f.is_accepted=true)
            """;

    private static final String FIND_COMMON_FRIENDS_QUERY = """
            (%s) INTERSECT (%s)
            """.formatted(FIND_USER_FRIENDS_QUERY, FIND_USER_FRIENDS_QUERY);

    private static final String INSERT_USER_QUERY = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = """
            UPDATE users
            SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?
            """;

    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    public UserDbRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> findAll() {
        return this.findMany(FIND_ALL_QUERY);
    }

    @Override
    public Collection<User> findUserFriends(Long id) {
        return this.findMany(FIND_USER_FRIENDS_QUERY, id, id);
    }

    @Override
    public Collection<User> findCommonFriends(Long userId, Long otherUserId) {
        return this.findMany(FIND_COMMON_FRIENDS_QUERY, userId, userId, otherUserId, otherUserId);
    }

    @Override
    public Optional<User> findById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User save(User user) {
        long id = this.insert(
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

    @Override
    public void deleteById(Long id) {
        this.update(DELETE_USER_QUERY, id);
    }
}
