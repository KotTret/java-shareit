package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    @GetMapping
    public List<Booking> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.findAllByBookerId(userId, state);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> get(@RequestHeader("X-Sharer-User-id") Long userId, @PathVariable Long id) {
        return new ResponseEntity<>(bookingMapper.toDto(bookingService.get(id, userId)), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody BookingDto dto) {
        Booking booking = bookingMapper.toEntity(dto);
        return new ResponseEntity<>(bookingMapper.toDto(bookingService.create(booking, userId, dto.getItemId())), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Validated @PathVariable Long bookingId,
                                                @RequestParam boolean approved) {

        return new ResponseEntity<>(bookingMapper.toDto(bookingService.setStatus(userId, bookingId, approved))
                , HttpStatus.OK);

    }
}
