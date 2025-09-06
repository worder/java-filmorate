package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("inMemoryUserRepository")
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    @Override
    public Collection<User> findAll() {
        return this.users.values().stream()
                .map(this::ensureUserName).toList();
    }

    @Override
    public Collection<User> findUserFriends(Long id) {
        return new ArrayList<>();
    }

    @Override
    public Collection<User> findCommonFriends(Long userId, Long otherUserId) {
        return new ArrayList<>();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id)).map(this::ensureUserName);
    }

    @Override
    public User save(User user) {
        long id = this.getNextId();
        User addedUser = user.toBuilder().id(id).build();
        this.users.put(id, addedUser);
        return addedUser;
    }

    @Override
    public User update(User user) {
        if (!this.users.containsKey(user.getId())) {
            throw new InternalServerException("Failed to update user");
        }

        User updatedUser = user.toBuilder().build();
        this.users.put(user.getId(), updatedUser);
        return updatedUser;
    }

    private long getNextId() {
        return ++this.lastId;
    }

    private User ensureUserName(User user) {
        String name = user.getName();
        if (name == null || name.isEmpty()) {
            return user.toBuilder()
                    .name(user.getLogin())
                    .build();
        }

        return user;
    }
}
