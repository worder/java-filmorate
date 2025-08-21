package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    Map<Integer, Set<Integer>> friendsList = new HashMap<>();

    private final UserStorage storage;

    private final BiFunction<User, User, User> addFriend = (User user1, User user2) -> {
        Set<Integer> currentFriends = user1.getFriends();
        Set<Integer> newFriends = new HashSet<>(currentFriends);
        newFriends.add(user2.getId());

        return user1.toBuilder().friends(newFriends).build();
    };

    private final BiFunction<User, User, User> removeFriend = (User user1, User user2) -> {
        Set<Integer> currentFriends = user1.getFriends();
        Set<Integer> newFriends = new HashSet<>(currentFriends);
        newFriends.remove(user2.getId());

        return user1.toBuilder().friends(newFriends).build();
    };


    public User addToFriends(Integer user1id, Integer user2id) {
        User user1 = storage.get(user1id);
        User user2 = storage.get(user2id);

        User updatedUser = addFriend.apply(user1, user2);
        storage.update(updatedUser);
        storage.update(addFriend.apply(user2, user1));
        return updatedUser;
    }

    public User removeFromFriends(Integer user1id, Integer user2id) {
        User user1 = storage.get(user1id);
        User user2 = storage.get(user2id);

        User updatedUser = removeFriend.apply(user1, user2);
        storage.update(updatedUser);
        storage.update(removeFriend.apply(user2, user1));
        return updatedUser;
    }

    public Set<User> getUserFriends(Integer userId) {
        User user = storage.get(userId);

        return user.getFriends()
                .stream()
                .map(storage::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(Integer userId, Integer otherUserId) {
        Set<User> user1friends = this.getUserFriends(userId);
        User user2 = storage.get(otherUserId);
        Set<Integer> user2friendIds = user2.getFriends();

        return user1friends
                .stream()
                .filter(u -> user2friendIds.contains(u.getId()))
                .collect(Collectors.toSet());
    }

}
