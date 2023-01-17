package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.util.UtilMergeProperty;

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
        User user = userStorage.get(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id"));
        log.info("Запрошена информация о пользователе: {}", user.getEmail());
        return user;
    }

    @Override
    public User create(User user) {
        checkEmail(user.getEmail());
        userStorage.add(user);
        log.info("Добавлен пользователь: {}", user.getEmail());
        return user;
    }

    @Override
    public User update(Integer userId, UserDto userDto) {
        if (userDto.getEmail() != null) {
            checkEmail(userDto.getEmail());
        }
        userDto.setId(userId);
        User user = get(userId);
        UtilMergeProperty.copyProperties(userDto, user);
        userStorage.update(user);
        log.info("Информация о пользователе обновлена: {}", user.getEmail());
        return user;
    }

    @Override
    public void delete(Integer userId) {
        userStorage.delete(userId);
    }

    private void checkEmail(String email) {
        if (userStorage.containsEmail(email)) {
            throw new ValidationException("Данный email уже занят");
        }
    }

}
