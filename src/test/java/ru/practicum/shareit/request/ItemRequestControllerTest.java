package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService service;

    @SneakyThrows
    @Test
    void create() {
        long userId = 1L;
        ItemRequestDtoShort dtoShortRequest = new ItemRequestDtoShort(1L, "desc", null, null);
        ItemRequestDtoShort dtoShortResponse = new ItemRequestDtoShort(null, "desc", userId, null);
        when(service.create(any())).thenReturn(dtoShortResponse);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dtoShortRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(dtoShortResponse), result);
    }

    @SneakyThrows
    @Test
    void getAllByRequester() {
        int from = 0;
        int size = 20;
        long userId = 1;
        ItemRequestDtoLong itemRequestDtoLong = new ItemRequestDtoLong(1L, "desc", userId, null, List.of());
        when(service.getAllByRequester(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDtoLong));
        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestDtoLong)), result);
    }

    @SneakyThrows
    @Test
    void getAllByRequester_whenSizeIsNotValid_thenConstraintViolationExceptionThrown() {
        int from = 0;
        int size = -1;
        long userId = 1;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(service, never()).getAllByRequester(any(), anyInt(), anyInt());

        size = 0;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(service, never()).getAllByRequester(any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllByRequester_whenFromIsNotValid_thenConstraintViolationExceptionThrown() {
        int from = -1;
        int size = 10;
        long userId = 1;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(service, never()).getAllByRequester(any(), anyInt(), anyInt());

    }

    @SneakyThrows
    @Test
    void getAll() {
        int from = 0;
        int size = 20;
        long userId = 1;
        ItemRequestDtoLong itemRequestDtoLong = new ItemRequestDtoLong(1L, "desc", userId, null, List.of());
        when(service.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDtoLong));
        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestDtoLong)), result);
    }

    @SneakyThrows
    @Test
    void getById() {
        long userId = 1L;
        long requestId = 1L;
        ItemRequestDtoLong itemRequestDtoLong = new ItemRequestDtoLong(1L, "desc", userId, null, List.of());
        when(service.getById(anyLong(), anyLong())).thenReturn(itemRequestDtoLong);

        String result = mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDtoLong), result);
    }
}