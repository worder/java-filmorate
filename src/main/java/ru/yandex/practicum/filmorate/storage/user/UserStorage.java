package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User add(User user);

    User get(Integer id);

    User update(User user);

    boolean hasUser(Integer id);

    Collection<User> getAll();
}
