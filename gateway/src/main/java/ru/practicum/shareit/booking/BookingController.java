package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                         @RequestParam(defaultValue = "ALL", name = "state") String s,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        BookingState state = BookingState.from(s).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + s));
        log.info("Get booking with state {}, userId={}, from={}, size={}", s, userId, from, size);
        return bookingClient.findAllByBookerId(userId, state, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-id") Long userId, @PathVariable Long id) {
        log.info("Get booking {}, userId={}", id, userId);
        return bookingClient.get(id, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-id") Long userId,
                                                @RequestParam(defaultValue = "ALL", name = "state") String s,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
        BookingState state = BookingState.from(s).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + s));
        log.info("Get bookings by owner with state {}, ownerId={}, from={}, size={}", s, userId, from, size);
        return bookingClient.getAllByOwner(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody BookItemRequestDto dto) {
        log.info("Create booking {}, userId={}", dto, userId);
        return bookingClient.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam boolean approved) {
        log.info("Approve bookingId={}, ownerId={}", bookingId, userId);
        return bookingClient.setStatus(userId, bookingId, approved);
    }

}
