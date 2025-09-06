package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserFriendsService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
//    private final UserRepository storage;
    private final UserService userService;
    private final UserFriendsService friendService;

    @GetMapping
    public Collection<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return userService.createUser(user);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UpdateUserRequest user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        friendService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        friendService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getFriends(@PathVariable Long id) {
        return friendService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getFriendsCommon(@PathVariable Long id, @PathVariable Long otherId) {
        System.out.println(id + " - " + otherId);
        return friendService.getCommonFriends(id, otherId);
    }
}
