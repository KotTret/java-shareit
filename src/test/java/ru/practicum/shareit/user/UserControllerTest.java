package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @SneakyThrows
    @Test
    void getById() {
        long userId = 1L;
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).get(userId);
    }

    @SneakyThrows
    @Test
    void getAll() {
        int from = 0;
        int size = 20;
        mockMvc.perform(get("/users")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).getAll(from, size);
    }

    @SneakyThrows
    @Test
    void getAll_whenRequestParamIsDefault() {

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).getAll(0, 10);
    }

    @SneakyThrows
    @Test
    void create_whenUserIsValid_thenReturnedOk() {
        UserDto userToCreate = new UserDto(null, "kot@tret.ru", "Kot");
        when(userService.create(userToCreate)).thenReturn(userToCreate);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userToCreate), result);
    }

    @SneakyThrows
    @Test
    void create_whenUserNameIsNotValid_thenReturnedBadRequest() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto(null, "kot@tret.ru", null);

        mockMvc.perform(post("/users", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        userToUpdate.setName("  ");
        mockMvc.perform(post("/users", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(userToUpdate);
    }

    @SneakyThrows
    @Test
    void create_whenUserEmailIsNotValid_thenReturnedBadRequest() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto(null, "kot.ru", "Kot");

        mockMvc.perform(post("/users", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        userToUpdate.setEmail("     ");
        mockMvc.perform(post("/users", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        userToUpdate.setEmail(null);
        mockMvc.perform(post("/users", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(userToUpdate);
    }

    @SneakyThrows
    @Test
    void update_whenUserIsValid_thenReturnedOk() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto(null, "kot@tret.ru", "Kot");
        when(userService.update(userId, userToUpdate)).thenReturn(userToUpdate);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userToUpdate), result);
    }

    @SneakyThrows
    @Test
    void update_whenUserNameIsNotValid_thenReturnedBadRequest() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto(null, null, "");

        mockMvc.perform(patch("/users/{userId}", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).update(userId, userToUpdate);
    }

    @SneakyThrows
    @Test
    void update_whenUserEmailIsEmpty_thenReturnedBadRequest() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto(1L, "", null);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(userId, userToUpdate);
    }

    @SneakyThrows
    @Test
    void update_whenUserEmailIsNotValid_thenReturnedBadRequest() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto(1L, "kot.ru", null);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(userId, userToUpdate);
    }

    @SneakyThrows
    @Test
    void deleteById() {
        long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).delete(userId);
    }

}