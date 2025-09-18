package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository storage;

    public UserDto createUser(NewUserRequest request) {
        User newUser = UserMapper.mapToUser(request);

        newUser = storage.save(newUser);
        log.info("Created user: {} from data: {}", newUser, request);

        return UserMapper.mapToUserDto(newUser);
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
        User user = storage.findById(request.getId())
                .map(u -> UserMapper.updateUserFields(u, request))
                .orElseThrow(() -> new NotFoundException("User update failed, user not found"));

        storage.update(user);
        log.info("Updated user: {} from data: {}", user, request);

        return UserMapper.mapToUserDto(user);
    }

    public void deleteUser(Long id) {
        if (!userExists(id)) {
            throw new NotFoundException("User deletion failed, user not found");
        }
        storage.deleteById(id);
        log.info("Deleted user id={}", id);
    }

    public boolean userExists(Long userId) {
        return storage.findById(userId).isPresent();
    }
}
