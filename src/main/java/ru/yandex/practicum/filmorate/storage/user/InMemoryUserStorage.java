package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int lastId = 0;

    public User add(User user) {
        int id = this.getNextId();
        User addedUser = this.ensureUserName(user)
                .toBuilder()
                .id(id)
                .friends(new HashSet<>())
                .build();

        this.users.put(id, addedUser);
        log.debug("Created new user: {}", addedUser);

        return addedUser;
    }

    public User get(Integer id) {
        if (!users.containsKey(id)) {
            log.debug("Trying to get non-existing user with id: {}", id);
            throw new NotFoundException("User not found, id:" + id);
        }
        return users.get(id);
    }

    public User update(User user) {
        if (user == null) {
            throw new InvalidArgumentException("User is null");
        }
        if (!this.users.containsKey(user.getId())) {
            log.warn("Trying to update non-existing user: {}", user);
            throw new NotFoundException("User not found, id:" + user.getId());
        }

        User currentUser = this.users.get(user.getId());

        Set<Integer> friends = user.getFriends();
        if (friends == null) {
            friends = currentUser.getFriends();
        }

        User updatedUser = this.ensureUserName(user.toBuilder()
                .friends(friends)
                .build());

        this.users.put(user.getId(), updatedUser);
        log.debug("Updated user: {} to {}", currentUser, updatedUser);

        return updatedUser;
    }

    public boolean hasUser(Integer id) {
        return users.containsKey(id);
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
