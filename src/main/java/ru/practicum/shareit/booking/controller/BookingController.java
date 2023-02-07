package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShortRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.model.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;


    @GetMapping
    public List<BookingDto> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                   @RequestParam(defaultValue = "ALL", name = "state") String s,
                                   @PositiveOrZero @RequestParam(defaultValue = "0")   int from,
                                   @Positive @RequestParam(defaultValue = "10")  int size) {
            return bookingService.findAllByBookerId(userId, isCorrectState(s), from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> get(@RequestHeader("X-Sharer-User-id") Long userId, @PathVariable Long id) {
        return new ResponseEntity<>(bookingService.get(id, userId), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-id") Long userId,
                                          @RequestParam(defaultValue = "ALL", name = "state") String s,
                                          @PositiveOrZero @RequestParam(defaultValue = "0")   int from,
                                          @Positive @RequestParam(defaultValue = "10")  int size) {

            return bookingService.findAllByOwnerId(userId, isCorrectState(s), from, size);
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody BookingDtoShortRequest dto) {
        return new ResponseEntity<>(bookingService.create(dto, userId, dto.getItemId()), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> setStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam boolean approved) {

        return new ResponseEntity<>(bookingService.setStatus(userId, bookingId, approved), HttpStatus.OK);

    }

    private State isCorrectState(String s) {
        State state = State.from(s);
        if (state == null) {
            throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        } else {
            return state;
        }
    }
}
