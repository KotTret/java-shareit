package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private  final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable Integer userId) {
        return userService.get(userId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user)  {
        return userService.create(user);
    }
    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Integer userId) {
        userService.delete(userId);
    }

}
