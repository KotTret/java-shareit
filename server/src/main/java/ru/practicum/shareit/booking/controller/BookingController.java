package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShortRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;


import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @GetMapping
    public List<BookingDto> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                   @RequestParam(name = "state") String s,
                                   @RequestParam int from,
                                   @RequestParam int size) {
        return bookingService.findAllByBookerId(userId, State.valueOf(s), from, size);
    }

    @GetMapping("/{id}")
    public BookingDto get(@RequestHeader("X-Sharer-User-id") Long userId, @PathVariable Long id) {
        return bookingService.get(userId, id);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-id") Long userId,
                                          @RequestParam(name = "state") String s,
                                          @RequestParam int from,
                                          @RequestParam int size) {

        return bookingService.findAllByOwnerId(userId, State.valueOf(s), from, size);
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody BookingDtoShortRequest dto) {
        return bookingService.create(dto, userId, dto.getItemId());
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable Long bookingId,
                                @RequestParam boolean approved) {

        return bookingService.setStatus(userId, bookingId, approved);
    }

}
