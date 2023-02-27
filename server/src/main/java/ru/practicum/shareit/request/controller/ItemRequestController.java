package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoShort create(@RequestHeader("X-Sharer-User-id") Long userId,
                                      @RequestBody ItemRequestDtoShort itemRequestDto) {
        itemRequestDto.setRequesterId(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        return itemRequestService.create(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDtoLong> getAllByRequester(@RequestHeader("X-Sharer-User-id") Long userId,
                                                      @RequestParam int from,
                                                      @RequestParam int size) {
        return itemRequestService.getAllByRequester(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoLong> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                           @RequestParam int from,
                                           @RequestParam int size) {
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoLong getById(@RequestHeader("X-Sharer-User-id") Long userId,
                                      @PathVariable Long requestId) {
        return itemRequestService.getById(userId, requestId);
    }
}
