package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.UtilMergeProperty;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        log.info("Текущее количество пользователей: {}", users.size());
        return UserMapper.toDtoList(users);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public UserDto get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id"));
        log.info("Запрошена информация о пользователе: {}", user.getEmail());
        return UserMapper.toDto(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Данный email уже занят", e);
        }
        log.info("Добавлен пользователь: {}", user.getEmail());
        return UserMapper.toDto(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setId(userId);
        User userTarget = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id"));
        try {
            UtilMergeProperty.copyProperties(user, userTarget);
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Данный email уже занят", e);
        }
        log.info("Информация о пользователе обновлена: {}", user.getEmail());
        return UserMapper.toDto(userTarget);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
