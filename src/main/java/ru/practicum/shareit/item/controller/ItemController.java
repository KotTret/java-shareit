package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
       return itemService.getAll(userId);

    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> get(@PathVariable Long itemId) {
        return new ResponseEntity<>(ItemMapper.toDto(itemService.get(itemId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Validated(Create.class) @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.toEntity(itemDto);
        return new ResponseEntity<>(ItemMapper.toDto(itemService.create(userId, item)), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Validated(Update.class) @RequestBody ItemDto itemDto,
                                          @PathVariable Long itemId) {
        return new ResponseEntity<>(ItemMapper.toDto(itemService.update(userId, itemId, itemDto)), HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        if (!text.isBlank()) {
            List<Item> items = itemService.search(text);
            return items.stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
