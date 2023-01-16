package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.util.generator.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, Item> items = new HashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public List<Item> getAll(Integer userId) {
        if (userId == null) {
            return new ArrayList<>(items.values());
        } else {
            return items.values()
                    .stream()
                    .filter(o -> Objects.equals(o.getOwnerId(), userId)).collect(Collectors.toList());
        }
    }

    @Override
    public Item get(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public void add(Item item) {
        item.setId(idGenerator.getId());
        items.put(item.getId(), item);
    }

    @Override
    public void update(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public boolean containsId(Integer id) {
        return items.containsKey(id);
    }

    @Override
    public List<Item> search(String text) {
        return items.values()
                .stream()
                .filter(o -> (o.getName().toLowerCase().contains(text.toLowerCase())
                        || o.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && o.getAvailable().equals(Boolean.TRUE))
                .collect(Collectors.toList());
    }
}
