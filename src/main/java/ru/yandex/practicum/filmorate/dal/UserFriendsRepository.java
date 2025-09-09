package ru.yandex.practicum.filmorate.dal;

public interface UserFriendsRepository {
    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);
}
