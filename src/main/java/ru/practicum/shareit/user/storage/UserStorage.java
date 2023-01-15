package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User get(Integer userId);

    void add(User user);

    void update(User user);

    void delete(Integer userId);

    boolean containsId(Integer id);

    boolean containsEmail(String email);

}
