package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get items by owner, ownerId={}, from={}, size={}", userId, from, size);
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                                      @PathVariable Long itemId) {
        log.info("Get itemId={}, userId={}", itemId, userId);
        return itemClient.get(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Create item {}, userId={}", itemDto, userId);
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @Validated(Update.class) @RequestBody ItemDto itemDto,
                                       @PathVariable Long itemId) {
        log.info("Update item {} with itemId={}, userId={}", itemDto, itemId, userId);
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String text,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {
        if (!text.isBlank()) {
            log.info("Get items by tag={}, from={}, size={}", text, from, size);
            return itemClient.search(text, from, size);
        } else {
            return new ResponseEntity<Object>(List.of(), HttpStatus.OK);
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-id") Long userId,
                                         @PathVariable(name = "id") Long itemId,
                                         @Valid @RequestBody CommentDto commentDto) {
        log.info("Create comment, itemId={}, authorId={}", itemId, userId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
