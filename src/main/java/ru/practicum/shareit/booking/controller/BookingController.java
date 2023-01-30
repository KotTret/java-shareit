package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @GetMapping
    public List<BookingDto> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                   @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.findAllByBookerId(userId, state);
        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> get(@RequestHeader("X-Sharer-User-id") Long userId, @PathVariable Long id) {
        return new ResponseEntity<>(BookingMapper.toDto(bookingService.get(id, userId)), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-id") Long userId,
                                          @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.findAllByOwnerId(userId, state);
        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody BookingDtoShort dto) {
        Booking booking = BookingMapper.toEntity(dto);
        return new ResponseEntity<>(BookingMapper.toDto(bookingService.create(booking, userId, dto.getItemId())), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Validated @PathVariable Long bookingId,
                                                @RequestParam boolean approved) {

        return new ResponseEntity<>(BookingMapper.toDto(bookingService.setStatus(userId, bookingId, approved)),
                HttpStatus.OK);

    }
}
