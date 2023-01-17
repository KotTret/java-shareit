package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    List<Item> getAll(Integer userId);

    Optional<Item> get(Integer itemId);

    void add(Item item);

    void update(Item item);

    boolean containsId(Integer id);

    List<Item> search(String text);
}
