package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    long idUser = 1;

    @BeforeEach
    private void addUser() {
        User user = new User(1L, "user.user@ru.ru", "User");
        idUser = userRepository.save(user).getId();
    }

    @Test
    void getAll() {
        List<UserDto> actualDtoList = userService.getAll(0, 10);
        assertEquals(1, actualDtoList.size());
    }

    @Test
    void get() {
        UserDto actualDto = userService.get(idUser);

        assertEquals(idUser, actualDto.getId());
        assertEquals("user.user@ru.ru", actualDto.getEmail());
        assertEquals("User", actualDto.getName());
    }

    @Test
    void create_whenOk_thenSavedUserDto() {
        UserDto userDtoWithoutId = new UserDto(null, "kot.tret@ru.ru", "Kot");

        UserDto actualDto = userService.create(userDtoWithoutId);

        assertEquals(idUser + 1, actualDto.getId());
        assertEquals("kot.tret@ru.ru", actualDto.getEmail());
        assertEquals("Kot", actualDto.getName());
    }

    @Test
    void update() {
        UserDto dtoForUpdate = new UserDto(null, "kot.tret@ru.ru", "Kot");

        UserDto expectedDto = new UserDto(idUser, "kot.tret@ru.ru", "Kot");
        UserDto actualDto = userService.update(idUser, dtoForUpdate);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void delete() {
        userService.delete(idUser);
        List<UserDto> actualDtoList = userService.getAll(0, 10);

        assertEquals(List.of(), actualDtoList);
    }
}