package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {


    List<ItemDto> getAll(Long userId);

    Item get(Long itemId);

    Item create(Long userId, Item item);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    List<Item> search(String text);

}
