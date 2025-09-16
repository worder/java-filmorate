package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> findAll();

    Collection<User> findUserFriends(Long id);

    Collection<User> findCommonFriends(Long userId, Long otherUserId);

    Optional<User> findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
