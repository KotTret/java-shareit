package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDtoRequest;
import ru.practicum.shareit.item.comment.CommentDtoResponse;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponseLong;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private  ItemDtoRequest itemDtoRequest;
    private  ItemDtoResponseLong itemDtoResponseLong;
    private  ItemDtoResponseShort itemDtoResponseShort;
    private  CommentDtoRequest commentDtoRequest;
    private  CommentDtoResponse commentDtoResponse;

    @BeforeEach
    void create() {
        itemDtoRequest = new ItemDtoRequest(1L, "itemName", "description", true, 1L);
        itemDtoResponseLong = new ItemDtoResponseLong(1L, "itemName", "description", true,
                null, null, null);
        itemDtoResponseShort = new ItemDtoResponseShort(1L, "itemName", "description",
                true, 1L);

        commentDtoRequest = new CommentDtoRequest(1L, "comment");
        commentDtoResponse = new CommentDtoResponse(1L, "comment", null, 1L,
                "Kot", null);
    }

    @SneakyThrows
    @Test
    void getAll() {
        int from = 0;
        int size = 20;
        long userId = 1;
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService).getAll(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getAll_whenRequestParamIsDefault() {
        long userId = 1;
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService).getAll(userId, 0, 10);
    }

    @SneakyThrows
    @Test
    void getAll_whenRequestParamIsNotValid() {
        long userId = 1;
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(itemService, never()).getAll(userId, -1, 1);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(itemService, never()).getAll(userId, 1, -1);
    }

    @SneakyThrows
    @Test
    void getById_whenWithUserId_thenReturnedOk() {
        long itemId = 1;
        long userId = 1;
        when(itemService.get(anyLong(), anyLong())).thenReturn(itemDtoResponseLong);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtoResponseLong), result);
        verify(itemService).get(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getById_whenWithoutUserId_thenReturnedOk() {
        long itemId = 1;
        when(itemService.get(itemId, null)).thenReturn(itemDtoResponseLong);

        String result = mockMvc.perform(get("/items/{itemId}", itemId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtoResponseLong), result);
        verify(itemService).get(itemId, null);
    }

    @SneakyThrows
    @Test
    void create_whenUserIsValid_thenReturnedOk() {
        when(itemService.create(anyLong(), any())).thenReturn(itemDtoResponseShort);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtoResponseShort), result);
    }

    @SneakyThrows
    @Test
    void create_whenUserIsNotValid_thenReturnedBadRequest() {
        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoRequest)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).create(any(), any());

    }

    @SneakyThrows
    @Test
    void create_whenItemIsNotValid_thenReturnedBadRequest() {
        ItemDtoRequest itemDtoRequestIsNotValid = new ItemDtoRequest(1L, "",
                "description", true, 1L);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoRequestIsNotValid)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).create(any(), any());

        itemDtoRequestIsNotValid = new ItemDtoRequest(1L, "Name",
                "description", true, -1L);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoRequestIsNotValid)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).create(any(), any());

        itemDtoRequestIsNotValid = new ItemDtoRequest(1L, "Name",
                "description", null, 1L);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoRequestIsNotValid)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).create(any(), any());

        itemDtoRequestIsNotValid = new ItemDtoRequest(1L, "Name",
                "", true, 1L);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoRequestIsNotValid)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).create(any(), any());
    }


    @SneakyThrows
    @Test
    void update() {
        long itemId = 1;
        when(itemService.update(anyLong(), anyLong(), any())).thenReturn(itemDtoResponseShort);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtoResponseShort), result);
    }

    @SneakyThrows
    @Test
    void search_whenTextIsNotEmpty() {
        int from = 0;
        int size = 20;
        String text = "text";
        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService).search(text, from, size);
    }

    @SneakyThrows
    @Test
    void search_whenTextIsEmpty() {
        int from = 0;
        int size = 20;
        String text = "";
        String result = mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(List.of()), result);
    }

    @SneakyThrows
    @Test
    void addComment() {
        long id = 1;
        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDtoResponse);

        String result = mockMvc.perform(post("/items/{id}/comment", id)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDtoRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDtoResponse), result);
    }
}