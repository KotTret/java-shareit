package ru.practicum.shareit.user.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.util.generator.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private final Map<Integer,String> emails = new HashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> get(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void add(User user) {
        user.setId(idGenerator.getId());
        users.put(user.getId(), user);
        emails.put(user.getId(),user.getEmail());
    }

    @Override
    public void update(User user) {
        emails.put(user.getId(), user.getEmail());
    }

    @Override
    public void delete(Integer userId) {
        users.remove(userId);
        emails.remove(userId);
        //add remove Items
    }

    @Override
    public boolean containsId(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public boolean containsEmail(String email) {
        return emails.containsValue(email);
    }
}
