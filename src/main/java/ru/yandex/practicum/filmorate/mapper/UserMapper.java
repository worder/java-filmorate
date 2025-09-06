package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    public static User mapToUser(NewUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .login(request.getLogin())
                .name(request.getName())
                .birthday(request.getBirthday())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        User.UserBuilder userBuilder = user.toBuilder();
        if (request.hasEmail()) {
            userBuilder.email(request.getEmail());
        }
        if (request.hasLogin()) {
            userBuilder.login(request.getLogin());
        }
        if (request.hasName()) {
            userBuilder.name(request.getName());
        }
        if (request.hasBirthday()) {
            userBuilder.birthday(request.getBirthday());
        }
        return userBuilder.build();
    }
}
