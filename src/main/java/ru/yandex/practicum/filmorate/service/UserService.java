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
}
