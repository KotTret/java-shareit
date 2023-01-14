package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User get(Integer userId);

    User create(User user);

    User update(User user);

    void delete(Integer userId);
}