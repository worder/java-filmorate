package ru.yandex.practicum.filmorate.dal.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserFriendsRepository;

@Repository
@RequiredArgsConstructor
public class UserFriendsDbRepository implements UserFriendsRepository {
    private static final String INSERT_QUERY = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";

    private final JdbcTemplate db;

    @Override
    public void addFriend(Long userId, Long friendId) {
        db.update(INSERT_QUERY, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        db.update(DELETE_QUERY, userId, friendId);
    }
}
