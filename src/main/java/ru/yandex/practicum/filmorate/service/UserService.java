package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository storage;

    public UserDto createUser(NewUserRequest request) {
        User newUser = UserMapper.mapToUser(request);
        return UserMapper.mapToUserDto(storage.save(newUser));
    }

    public UserDto getUserById(Long id) {
        return storage.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<UserDto> getAllUsers() {
        return storage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto updateUser(UpdateUserRequest request) {
        Optional<User> user = storage.findById(request.getId())
                .map(u -> UserMapper.updateUserFields(u, request));

        if (user.isPresent()) {
            storage.update(user.get());
            return UserMapper.mapToUserDto(user.get());
        }

        // TODO: add logging
        throw new NotFoundException("User update failed, user not found");
    }

    public boolean userExists(Long userId) {
        return storage.findById(userId).isPresent();
    }


//    private final BiFunction<User, User, User> addFriend = (User user1, User user2) -> {
//        Set<Integer> currentFriends = user1.getFriends();
//        Set<Integer> newFriends = new HashSet<>(currentFriends);
//        newFriends.add(user2.getId());
//
//        return user1.toBuilder().friends(newFriends).build();
//    };
//
//    private final BiFunction<User, User, User> removeFriend = (User user1, User user2) -> {
//        Set<Integer> currentFriends = user1.getFriends();
//        Set<Integer> newFriends = new HashSet<>(currentFriends);
//        newFriends.remove(user2.getId());
//
//        return user1.toBuilder().friends(newFriends).build();
//    };
//
//
//    public User addToFriends(Integer user1id, Integer user2id) {
//        User user1 = storage.get(user1id);
//        User user2 = storage.get(user2id);
//
//        User updatedUser = addFriend.apply(user1, user2);
//        storage.update(updatedUser);
//        storage.update(addFriend.apply(user2, user1));
//        return updatedUser;
//    }
//
//    public User removeFromFriends(Integer user1id, Integer user2id) {
//        User user1 = storage.get(user1id);
//        User user2 = storage.get(user2id);
//
//        User updatedUser = removeFriend.apply(user1, user2);
//        storage.update(updatedUser);
//        storage.update(removeFriend.apply(user2, user1));
//        return updatedUser;
//    }
//
//    public Set<User> getUserFriends(Integer userId) {
//        User user = storage.get(userId);
//
//        return user.getFriends()
//                .stream()
//                .map(storage::get)
//                .collect(Collectors.toSet());
//    }
//
//    public Set<User> getCommonFriends(Integer userId, Integer otherUserId) {
//        Set<User> user1friends = this.getUserFriends(userId);
//        User user2 = storage.get(otherUserId);
//        Set<Integer> user2friendIds = user2.getFriends();
//
//        return user1friends
//                .stream()
//                .filter(u -> user2friendIds.contains(u.getId()))
//                .collect(Collectors.toSet());
//    }

}
