package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.page.MyPageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    UserDto userDtoWithoutId;

    @Test
    void getAll_whenOk_thenFoundUserDtoList() {
        User user1 = new User(1L, "kot.tret@ru.ru", "Kot");
        User user2 = new User(2L, "kot.tret@ru.ru", "Kot");
        MyPageRequest pageable = new MyPageRequest(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> page = new PageImpl<>(List.of(user1, user2));
        when(userRepository.findAll(pageable))
                .thenReturn(page);

        List<UserDto> expectedDtoList = UserMapper.toDtoList(List.of(user1, user2));
        List<UserDto> actualDtoList = userService.getAll(1, 10);

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void get_whenOk_thenFoundUserDto() {
        User user = new User(1L, "kot.tret@ru.ru", "Kot");
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        UserDto expectedDto = new UserDto(1L, "kot.tret@ru.ru", "Kot");
        UserDto actualDto = userService.get(user.getId());

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void get_whenIdNotFound_thenObjectNotFoundExceptionThrown() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        long userId = 999;

        assertThrows(ObjectNotFoundException.class, () -> userService.get(userId));
    }

    @Test
    void create_whenOk_thenSavedUserDto() {
        User user = new User(1L, "kot.tret@ru.ru", "Kot");
        userDtoWithoutId = new UserDto(null, "kot.tret@ru.ru", "Kot");
        when(userRepository.save(any()))
                .thenReturn(user);

        UserDto expectedDto = new UserDto(1L, "kot.tret@ru.ru", "Kot");
        UserDto actualDto = userService.create(userDtoWithoutId);

       assertEquals(expectedDto, actualDto);
    }

    @Test
    void create_whenEmailNotValid_thenValidationExceptionThrown() {
        userDtoWithoutId = new UserDto(null, "kot.tret@ru.ru", "Kot");
        when(userRepository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(ValidationException.class, () -> userService.create(userDtoWithoutId));
    }

    @Test
    void update_whenEmailNotValid_thenValidationExceptionThrown() {
        UserDto dto = new UserDto(null, "kot.tret@ru.ru", "Kot");
        User user = new User(1L, "kot.tret@ru.ru", "Kot");
        long userId = 1L;
        doThrow(DataIntegrityViolationException.class).when(userRepository).flush();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () -> userService.update(userId, dto));
    }

    @Test
    void update_whenOk_thenUpdateUser() {
        UserDto dto = new UserDto(null, "dog.tret@ru.ru", "Kot");
        long userId = 1L;
        User user = new User(1L, "kot.tret@ru.ru", "Kot");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto expectedDto = new UserDto(1L, "dog.tret@ru.ru", "Kot");
        UserDto actualDto = userService.update(userId, dto);

        assertEquals(expectedDto, actualDto);
        verify(userRepository, times(1)).flush();
    }

    @Test
    void update_whenIdNotFound_thenObjectNotFoundExceptionThrown() {
        UserDto dto = new UserDto(999L, "kot.tret@ru.ru", "Kot");
        when(userRepository.findById(dto.getId()))
                .thenReturn(Optional.empty());
        long userId = 999;

        assertThrows(ObjectNotFoundException.class, () -> userService.update(userId, dto));
    }

    @Test
    void delete() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}