package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> get(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.get(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Validated(Create.class) @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.create(userDto), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@Validated(Update.class) @RequestBody UserDto userDto,
                                          @PathVariable Long userId) {
        return new ResponseEntity<>(userService.update(userId, userDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
