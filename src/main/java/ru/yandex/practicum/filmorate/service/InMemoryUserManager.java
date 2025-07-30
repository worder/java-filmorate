package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
public class InMemoryUserManager implements UserManager {

    private final Map<Integer, User> users = new HashMap<>();
    private int lastId = 0;

    public User add(User user) {
        int id = this.getNextId();
        User addedUser = this.ensureUserName(user)
                .toBuilder()
                .id(id)
                .build();

        this.users.put(id, addedUser);
        return addedUser;
    }

    public Optional<User> get(Integer id) {
        return Optional.of(users.get(id));
    }

    public User update(User user) {
        this.users.put(user.getId(), this.ensureUserName(user));
        return user;
    }

    public Collection<User> getAll() {
        return new ArrayList<>(this.users.values());
    }

    private int getNextId() {
        return ++this.lastId;
    }

    private User ensureUserName(User user) {
        String name = user.getName();
        if (name == null || name.isEmpty()) {
            log.debug("User name is empty for user id: {}, using login: {}", user.getId(), user.getLogin());
            return user.toBuilder()
                    .name(user.getLogin())
                    .build();
        }

        return user;
    }
}
