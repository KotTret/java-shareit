package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.util.UtilMergeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;


    @Override
    public List<Item> getAll(Integer userId) {
        List<Item> items = itemStorage.getAll(userId);
        log.info("Текущее количество вещей у пользователя - {}: {}", userId, items.size());
        return items;
    }

    @Override
    public Item get(Integer itemId) {
        checkItem(itemId);
        Item item = itemStorage.get(itemId);
        log.info("Запрошена информация о Item:name - {}, id - {}", item.getName(), item.getId());
        return item;
    }

    @Override
    public Item create(Integer userId, Item item) {
        checkUser(userId);
        item.setOwnerId(userId);
        itemStorage.add(item);
        log.info("Добавлен Item:name - {}, id - {}", item.getName(), item.getId());
        return item;
    }

    @Override
    public Item update(Integer userId, Integer itemId, ItemDto itemdto) {
        checkItemForUser(userId, itemId);
        Item item = get(itemId);
        UtilMergeProperty.copyProperties(itemdto, item);
        itemStorage.update(item);
        log.info("Информация о Item обнолвена:name - {}, id - {}", item.getName(), item.getId());
        return item;
    }

    @Override
    public List<Item> search(String text) {
        log.info("Выполнен поиск Item по значению - {}", text);
        if (text != null && !text.isBlank()) {
            return itemStorage.search(text);
        } else {
            return new ArrayList<>();
        }
    }

    private void checkItem(Integer id) {
        if (!itemStorage.containsId(id)) {
            throw new ObjectNotFoundException("Item не найден, проверьте верно ли указан Id");
        }
    }

    private void checkUser(Integer id) {
        if (!userStorage.containsId(id)) {
            throw new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id");
        }
    }

    private void checkItemForUser(Integer userId, Integer itemId) {
        checkItem(itemId);
        if (!userStorage.containsId(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id");
        }
        if (!Objects.equals(itemStorage.get(itemId).getOwnerId(), userId)) {
            throw new ObjectNotFoundException(String.format("Item c id = %d не найден для пользователя c id = %d", itemId, userId));
        }
    }
}
