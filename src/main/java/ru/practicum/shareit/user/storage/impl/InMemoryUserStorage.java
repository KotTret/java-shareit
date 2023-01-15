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

    private final HashSet<String> emails = new HashSet<>();
    private final IdGenerator idGenerator;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(Integer userId) {
       return users.get(userId);
    }

    @Override
    public void add(User user) {
        user.setId(idGenerator.getId());
        users.put(user.getId(), user);
        emails.addAll(users.values().stream().map(User::getEmail).collect(Collectors.toSet()));
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
        emails.clear();
        emails.addAll(users.values().stream().map(User::getEmail).collect(Collectors.toSet()));
    }

    @Override
    public void delete(Integer userId) {
        emails.remove(users.remove(userId).getEmail());
    }

    @Override
    public boolean containsId(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public boolean containsEmail(String email) {
        return emails.contains(email);
    }
}
