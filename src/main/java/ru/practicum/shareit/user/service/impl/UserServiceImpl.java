package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    @Override
    public List<User> getAll() {
        List<User> users = userStorage.getAll();
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @Override
    public User get(Integer userId) {
        checkUser(userId);
        User user = userStorage.get(userId);
        log.info("Запрошена информация о пользователе: {}", user.getEmail());
        return user;
    }

    @Override
    public User create(User user) {
        userStorage.add(user);
        log.info("Добавлен пользователь: {}", user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        userStorage.update(user);
        log.info("Информация о пользователе обнолвена: {}", user.getEmail());
        return user;
    }

    @Override
    public void delete(Integer userId) {
        checkUser(userId);
        userStorage.delete(userId);
    }

    private void checkUser(Integer id) {
        if (!userStorage.containsId(id)) {
            throw new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id");
        }
    }
}
