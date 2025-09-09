package ru.yandex.practicum.filmorate.dal.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserFriendsRepository;

import java.util.HashSet;

@Repository("inMemoryUserFriendsRepository")
@RequiredArgsConstructor
public class InMemoryUserFriendsRepository implements UserFriendsRepository {
    private final MemoryStorage storage;

    public void addFriend(Long userId, Long friendId) {
        if (!storage.userFriends.containsKey(userId)) {
            storage.userFriends.put(userId, new HashSet<>());
        }
        if (!storage.userFriendsPending.containsKey(friendId)) {
            storage.userFriendsPending.put(friendId, new HashSet<>());
        }
        storage.userFriends.get(userId).add(friendId);
        storage.userFriendsPending.get(friendId).add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        if (storage.userFriends.containsKey(userId)) {
            storage.userFriends.get(userId).remove(friendId);
        }
        if (storage.userFriendsPending.containsKey(friendId)) {
            storage.userFriendsPending.get(friendId).remove(userId);
        }
    }
}
