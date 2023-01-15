package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private  final UserMapper userMapper;
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
        checkEmail(user.getEmail());
        userStorage.add(user);
        log.info("Добавлен пользователь: {}", user.getEmail());
        return user;
    }

    @Override
    public User update(Integer userId, UserDto userDto) {
        checkUser(userId);
        if (userDto.getEmail() != null) {
            checkEmail(userDto.getEmail());
        }
        userDto.setId(userId);
        User user = userStorage.get(userId);
        BeanUtils.copyProperties(userDto, user, getNullPropertyNames(userDto));
        userStorage.update(user);

        log.info("Информация о пользователе обновлена: {}", user.getEmail());
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

    private void checkEmail(String email) {
        if (userStorage.containsEmail(email)) {
            throw new ValidationException("Данный email уже занят");
        }
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
