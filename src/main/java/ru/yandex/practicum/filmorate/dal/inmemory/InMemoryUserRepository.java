package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository("inMemoryUserRepository")
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final VolatileMemoryStorage storage;
    private long lastId = 0;

    @Override
    public Collection<User> findAll() {
        return storage.users.values().stream()
                .map(this::ensureUserName).toList();
    }

    @Override
    public Collection<User> findUserFriends(Long id) {
        if (storage.userFriends.containsKey(id)) {
            return storage.userFriends.get(id).stream()
                    .map(this::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<User> findCommonFriends(Long userId, Long otherUserId) {
        Set<Long> userFriends = storage.userFriends.get(userId);
        Set<Long> otherUserFriends = storage.userFriends.get(otherUserId);

        if (userFriends == null || otherUserFriends == null) {
            return new ArrayList<>();
        }

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.users.get(id)).map(this::ensureUserName);
    }

    @Override
    public User save(User user) {
        long id = this.getNextId();
        User addedUser = user.toBuilder().id(id).build();
        this.storage.users.put(id, addedUser);
        return addedUser;
    }

    @Override
    public User update(User user) {
        User updatedUser = user.toBuilder().build();
        storage.users.put(user.getId(), updatedUser);
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
