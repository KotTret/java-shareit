package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> getAll(Integer userId);

    Item get(Integer itemId);

    Item create(Integer userId, Item item);

    Item update(Integer userId, Item item);

   // List<Item> search(String query, String [] searchBy);
}
