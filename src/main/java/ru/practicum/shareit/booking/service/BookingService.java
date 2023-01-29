package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking, Long userId, Long itemId);

    Booking setStatus(Long userId, Long bookingId, boolean approved);

    Booking get(Long id, Long userId);

    List<Booking> findAllByBookerId(Long userId, State state);
}