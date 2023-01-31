package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShortRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingDtoShortRequest bookingDto, Long userId, Long itemId);

    BookingDto setStatus(Long userId, Long bookingId, boolean approved);

    BookingDto get(Long id, Long userId);

    List<BookingDto> findAllByBookerId(Long userId, State state);

    List<BookingDto> findAllByOwnerId(Long userId, State state);
}
