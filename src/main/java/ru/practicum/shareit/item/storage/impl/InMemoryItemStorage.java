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
    private final Map<Integer, List<Item>> userItemIndex = new LinkedHashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public List<Item> getAll(Integer userId) {
        if (!userItemIndex.containsKey(userId)) {
            return  List.of();
        } else {
            return (userItemIndex.get(userId));
        }
    }

    @Override
    public Optional<Item> get(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public void add(Item item) {
        item.setId(idGenerator.getId());
        items.put(item.getId(), item);
        final List<Item> items = userItemIndex.computeIfAbsent(item.getOwnerId(), k -> new ArrayList<>());
        items.add(item);
    }

    @Override
    public void update(Item item) {
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

    @Override
    public void deleteItemsForUser(Integer userId) {
        if (userItemIndex.containsKey(userId)) {
            userItemIndex.remove(userId)
                    .stream()
                    .map(Item::getId)
                    .forEach(items::remove);
        }
    }
}
