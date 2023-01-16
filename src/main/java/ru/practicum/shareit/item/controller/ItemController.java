package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Integer userId) {
        List<Item> items = itemService.getAll(userId);
        return items.stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> get(@PathVariable Integer itemId) {
        return new ResponseEntity<>(itemMapper.toDto(itemService.get(itemId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @Valid @RequestBody Item item) {
        return new ResponseEntity<>(itemMapper.toDto(itemService.create(userId, item)), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @Valid @RequestBody ItemDto itemDto, @PathVariable Integer itemId) {
        return new ResponseEntity<>(itemMapper.toDto(itemService.update(userId, itemId, itemDto)), HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        List<Item> items = itemService.search(text);
        return items.stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}
