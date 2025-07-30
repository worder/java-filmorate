package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserManager {
    User add(User user);

    Optional<User> get(Integer id);

    User update(User user);

    Collection<User> getAll();
}
