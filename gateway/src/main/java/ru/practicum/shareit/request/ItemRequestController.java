package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.util.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-id") Long userId,
                                         @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Create request {}, requestorId={}", itemRequestDto, userId);
        return itemRequestClient.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequester(@RequestHeader("X-Sharer-User-id") Long userId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get requests by requestor with requestorId={}", userId);
        return itemRequestClient.getAllByRequester(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get all requests by userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-id") Long userId,
                                          @PathVariable Long requestId) {
        log.info("Get requestId={}, userId={}", requestId, userId);
        return itemRequestClient.getById(userId, requestId);
    }
}
