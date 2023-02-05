package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDtoRequest;
import ru.practicum.shareit.item.comment.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponseLong;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoResponseLong> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId);

    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDtoResponseLong> get(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                                                   @PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.get(itemId, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDtoResponseShort> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @Validated(Create.class) @RequestBody ItemDtoRequest itemDto) {
        return new ResponseEntity<>(itemService.create(userId, itemDto), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDtoResponseShort> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @Validated(Update.class) @RequestBody ItemDtoRequest itemDto,
                                                      @PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.update(userId, itemId, itemDto), HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<ItemDtoResponseLong> search(@RequestParam("text") String text) {
        if (!text.isBlank()) {
            itemService.search(text);
            return itemService.search(text);
        } else {
            return List.of();
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDtoResponse> addComment(@RequestHeader("X-Sharer-User-id") Long userId,
                                                         @PathVariable(name = "id") Long itemId,
                                                         @Valid @RequestBody CommentDtoRequest commentDto) {
        return new ResponseEntity<>(itemService.createComment(itemId, userId, commentDto), HttpStatus.OK);
    }
}
