package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private  final UserService userService;

    private  final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAll() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Integer userId) {
        return userMapper.toDto(userService.get(userId));
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody User user)  {
        return userMapper.toDto(userService.create(user));
    }
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId) {
        return new ResponseEntity<>(userMapper.toDto(userService.update(userId, userDto)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Integer userId) {
        userService.delete(userId);
    }
}
