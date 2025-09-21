package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.dal.db.UserDbRepository;
import ru.yandex.practicum.filmorate.dal.db.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbRepository.class, UserRowMapper.class})
class UserDbRepositoryDeleteIT {

    private final UserDbRepository userRepo;
    private final JdbcTemplate jdbc;

    @Autowired
    UserDbRepositoryDeleteIT(UserDbRepository userRepo, JdbcTemplate jdbc) {
        this.userRepo = userRepo;
        this.jdbc = jdbc;
    }

    private long insertUser(String email, String login, String name, LocalDate birthday) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, email);
            ps.setString(2, login);
            ps.setString(3, name);
            ps.setDate(4, Date.valueOf(birthday));
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }

    private void insertFriendship(long userId, long friendId, boolean accepted) {
        jdbc.update(
                "INSERT INTO user_friends (user_id, friend_id, is_accepted) VALUES (?, ?, ?)",
                userId, friendId, accepted
        );
    }

    private int countUser(long id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE id=?", Integer.class, id);
        return n == null ? 0 : n;
    }

    private int countFriends(long id) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM user_friends WHERE user_id=? OR friend_id=?",
                Integer.class, id, id
        );
        return n == null ? 0 : n;
    }

    @Test
    void shouldDeleteUser() {
        long user1 = insertUser("a@example.com", "alice", "Alice", LocalDate.of(1990,1,1));
        long user2 = insertUser("b@example.com", "bob",   "Bob",   LocalDate.of(1991,2,2));
        long user3 = insertUser("c@example.com", "carol", "Carol", LocalDate.of(1992,3,3));

        insertFriendship(user1, user2, true);
        insertFriendship(user3, user1, false);

        assertThat(countUser(user1)).isEqualTo(1);
        assertThat(countFriends(user1)).isEqualTo(2);

        userRepo.deleteById(user1);

        assertThat(countUser(user1)).isZero();
        assertThat(countFriends(user1)).isZero();
        assertThat(countUser(user2)).isEqualTo(1);
        assertThat(countUser(user3)).isEqualTo(1);
    }

    @Test
    void shouldThrowInternalServerException() {
        assertThatThrownBy(() -> userRepo.deleteById(999L)).isInstanceOf(InternalServerException.class);
    }
}
