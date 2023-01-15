package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, Item> items = new HashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public List<Item> getAll(Integer userId) {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item get(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public void add(Item item) {
        item.setItemId(idGenerator.getId());
        items.put(item.getItemId(), item);
    }

    @Override
    public void update(Item item) {
        items.put(item.getItemId(), item);
    }

    @Override
    public boolean containsId(Integer id) {
        return items.containsKey(id);
    }

}
