package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InMemoryUserManager;
import ru.yandex.practicum.filmorate.service.UserManager;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    UserManager manager = new InMemoryUserManager();

    @GetMapping
    public Collection<User> getAll() {
        return manager.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User addedUser = manager.add(user);
        log.debug("Created user with id: {}", addedUser.getId());
        return addedUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (this.manager.get(user.getId()).isEmpty()) {
            log.warn("Try to update with unknown id: {}", user);
            throw new NotFoundException("Invalid id provided");
        }

        User updatedUser = manager.update(user);
        log.debug("Updated: {}", updatedUser);
        return updatedUser;
    }
}
