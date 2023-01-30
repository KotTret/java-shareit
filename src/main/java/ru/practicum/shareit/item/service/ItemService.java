package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {


    List<ItemDto> getAll(Long userId);

    ItemDto get(Long itemId, Long userId);

    Item create(Long userId, Item item);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    List<Item> search(String text);

    CommentDto createComment(Long itemId, Long userId, Comment comment);
}
