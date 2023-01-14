package ru.practicum.shareit.user.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.util.generator.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User get(Integer userId) {
        return null;
    }

    @Override
    public void add(User user) {
        user.setUserId(idGenerator.getId());
        users.put(user.getUserId(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public void delete(Integer userId) {
        users.remove(userId);
    }

    @Override
    public boolean containsId(Integer id) {
        return users.containsKey(id);
    }
}
