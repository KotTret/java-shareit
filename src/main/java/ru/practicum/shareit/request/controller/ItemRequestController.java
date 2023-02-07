package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.util.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDtoShort> create(@RequestHeader("X-Sharer-User-id") Long userId,
                                     @Validated(Create.class) @RequestBody ItemRequestDtoShort itemRequestDto) {
        itemRequestDto.setRequesterId(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        return new ResponseEntity<>(itemRequestService.create(itemRequestDto), HttpStatus.OK);
    }

    @GetMapping
    public List<ItemRequestDtoLong> getAllByRequester(@RequestHeader("X-Sharer-User-id") Long userId,
                                                      @PositiveOrZero     @RequestParam(defaultValue = "0")    int from,
                                                      @Positive    @RequestParam(defaultValue = "10") int size) {
        return itemRequestService.getAllByRequester(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoLong> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                           @PositiveOrZero  @RequestParam(defaultValue = "0")   int from,
                                           @Positive    @RequestParam(defaultValue = "10")  int size) {
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDtoLong> getById(@RequestHeader("X-Sharer-User-id") Long userId,
                                             @PathVariable Long requestId) {
        return new ResponseEntity<>(itemRequestService.getById(userId, requestId), HttpStatus.OK);
    }
}
