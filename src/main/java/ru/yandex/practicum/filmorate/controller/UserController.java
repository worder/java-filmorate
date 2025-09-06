package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
//    private final UserRepository storage;
    private final UserService userService;

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
//
//    @PutMapping("/{id}/friends/{friendId}")
//    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
//        return service.addToFriends(id, friendId);
//    }
//
//    @DeleteMapping("/{id}/friends/{friendId}")
//    public User removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
//        return service.removeFromFriends(id, friendId);
//    }
//
//    @GetMapping("/{id}/friends")
//    public Collection<User> getFriends(@PathVariable Integer id) {
//        return service.getUserFriends(id);
//    }
//
//    @GetMapping("/{id}/friends/common/{otherId}")
//    public Collection<User> getFriendsCommon(@PathVariable Integer id, @PathVariable Integer otherId) {
//        return service.getCommonFriends(id, otherId);
//    }
}
