package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User get(Integer userId);

    User create(User user);

    User update(Integer userId, UserDto userDto);

    void delete(Integer userId);
}