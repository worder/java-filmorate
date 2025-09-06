package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserFriendsRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserFriendsService {
    private final UserFriendsRepository friendsStorage;
    private final UserRepository userStorage;
    private final UserService userService;

    public void addFriend(Long userId, Long friendId) {
        if (userService.userExists(userId) && userService.userExists(friendId)) {
            friendsStorage.addFriend(userId, friendId);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        if (userService.userExists(userId) && userService.userExists(friendId)) {
            friendsStorage.removeFriend(userId, friendId);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public Collection<UserDto> getUserFriends(Long userId) {
        if (userService.userExists(userId)) {
            return userStorage.findUserFriends(userId).stream()
                    .map(UserMapper::mapToUserDto).toList();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public Collection<UserDto> getCommonFriends(Long userId, Long otherUserId) {
        if (userService.userExists(userId) && userService.userExists(otherUserId)) {
            return userStorage.findCommonFriends(userId, otherUserId).stream()
                    .map(UserMapper::mapToUserDto)
                    .toList();
        } else {
            throw new NotFoundException("User not found");
        }
    }
}
