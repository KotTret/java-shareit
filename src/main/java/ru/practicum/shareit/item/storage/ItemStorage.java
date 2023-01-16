package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    List<Item> getAll(Integer userId);

    Item get(Integer itemId);

    void add(Item item);

    void update(Item item);

    boolean containsId(Integer id);

    List<Item> search(String text);
}
