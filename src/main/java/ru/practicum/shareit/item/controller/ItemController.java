package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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
    public List<ItemDto> getAll(@RequestHeader("X-Later-User-Id") Integer userId) {
        List<Item> items = itemService.getAll(userId);
        return items.stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Integer itemId) {
        return itemMapper.toDto(itemService.get(itemId));
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Later-User-Id") Integer userId, @Valid @RequestBody Item item) {
        return itemMapper.toDto(itemService.create(userId, item));
    }

    @PutMapping("/{itemId}")
    public ItemDto put(@RequestHeader("X-Later-User-Id") Integer userId, @Valid @RequestBody Item item) {
        return itemMapper.toDto(itemService.update(userId, item));
    }

/*    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("query") String query, @RequestParam("by") String [] searchBy) {
        return itemService.search(query,searchBy);
    }*/
}
