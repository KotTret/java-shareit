package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShortRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingServiceImpl bookingService;

    LocalDateTime start = LocalDateTime.now().plusDays(1);
    LocalDateTime end = start.plusDays(2);

    @SneakyThrows
    @Test
    void getAll_whenIsCorrectState_thenReturnList() {
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.findAllByBookerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAll_whenFromIsNotValid_thenConstraintViolationExceptionThrown() {
        String fromStr = "fff";
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", fromStr)
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(bookingService, never()).findAllByBookerId(anyLong(), any(), anyInt(), anyInt());

        int fromInt = -1;
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(fromInt))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).findAllByBookerId(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAll_whenSizeIsNotValid_thenConstraintViolationExceptionThrown() {
        String sizeStr = "fff";
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(1))
                        .param("size", sizeStr))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(bookingService, never()).findAllByBookerId(anyLong(), any(), anyInt(), anyInt());

        int sizeInt = -1;
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(sizeInt)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).findAllByBookerId(anyLong(), any(), anyInt(), anyInt());

        sizeInt = 0;
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(sizeInt)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).findAllByBookerId(anyLong(), any(), anyInt(), anyInt());
    }



    @SneakyThrows
    @Test
    void getAll_whenIsNotCorrectState_thenBadRequestExceptionThrown() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "OLOLO")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).findAllByBookerId(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getById() {
        long bookingId = 1L;
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.get(anyLong(), anyLong())).thenReturn(bookingDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_whenIsCorrectState_thenReturnList() {
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.findAllByOwnerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_whenIsNotCorrectState_thenBadRequestExceptionThrown() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "OLOLO")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).findAllByOwnerId(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void create_whenIsOk() {
        long userId = 1L;
        long itemId = 1L;
        BookingDtoShortRequest bookingDtoShortRequest = new BookingDtoShortRequest(1L, start, end, itemId);
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.create(any(), anyLong(), anyLong())).thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoShortRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void create_whenStartIsNotFutureOrPresent_thenBadRequestExceptionThrown() {
        long userId = 1L;
        long itemId = 1L;
        BookingDtoShortRequest bookingDtoShortRequest = new BookingDtoShortRequest(1L, start.minusDays(3), end, itemId);
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.create(any(), anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoShortRequest)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).create(any(), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void create_whenEndIsNotFuture_thenBadRequestExceptionThrown() {
        long userId = 1L;
        long itemId = 1L;
        BookingDtoShortRequest bookingDtoShortRequest = new BookingDtoShortRequest(1L, start, end.minusDays(3), itemId);
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.create(any(), anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoShortRequest)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).create(any(), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void create_whenEndIsNull_thenBadRequestExceptionThrown() {
        long userId = 1L;
        long itemId = 1L;
        BookingDtoShortRequest bookingDtoShortRequest = new BookingDtoShortRequest(1L, start, null, itemId);
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.create(any(), anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoShortRequest)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).create(any(), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void create_whenEndIsNow_thenBadRequestExceptionThrown() {
        long userId = 1L;
        long itemId = 1L;
        BookingDtoShortRequest bookingDtoShortRequest = new BookingDtoShortRequest(1L, start, LocalDateTime.now(), itemId);
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.create(any(), anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoShortRequest)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).create(any(), anyLong(), anyLong());
    }


    @SneakyThrows
    @Test
    void setStatus() {
        long userId = 1L;
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.APPROVED, null, null);
        when(bookingService.setStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }
}