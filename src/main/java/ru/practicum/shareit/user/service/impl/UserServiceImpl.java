package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.UtilMergeProperty;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @Override
    public User get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id"));
        log.info("Запрошена информация о пользователе: {}", user.getEmail());
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User create(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Данный email уже занят", e);
        }
        log.info("Добавлен пользователь: {}", user.getEmail());
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User update(Long userId, User user) {
        user.setId(userId);
        User userTarget = get(userId);
        UtilMergeProperty.copyProperties(user, userTarget);
        log.info("Информация о пользователе обновлена: {}", user.getEmail());
        return userTarget;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Long userId) {
        userRepository.delete(get(userId));
    }

}
