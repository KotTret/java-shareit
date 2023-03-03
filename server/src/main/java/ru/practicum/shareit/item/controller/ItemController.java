package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDtoRequest;
import ru.practicum.shareit.item.comment.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponseLong;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoResponseLong> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam int from,
                                            @RequestParam int size) {
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponseLong get(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                                   @PathVariable Long itemId) {
        return itemService.get(itemId, userId);
    }

    @PostMapping
    public ItemDtoResponseShort create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody ItemDtoRequest itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponseShort update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody ItemDtoRequest itemDto,
                                       @PathVariable Long itemId) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDtoResponseLong> search(@RequestParam("text") String text,
                                            @RequestParam int from,
                                            @RequestParam int size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("/{id}/comment")
    public CommentDtoResponse addComment(@RequestHeader("X-Sharer-User-id") Long userId,
                                         @PathVariable(name = "id") Long itemId,
                                         @RequestBody CommentDtoRequest commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
