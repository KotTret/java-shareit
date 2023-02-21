package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDtoRequest;
import ru.practicum.shareit.item.comment.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponseLong;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;

import java.util.List;

public interface ItemService {


    List<ItemDtoResponseLong> getAll(Long userId, int from, int size);

    ItemDtoResponseLong get(Long itemId, Long userId);

    ItemDtoResponseShort create(Long userId, ItemDtoRequest itemDto);

    ItemDtoResponseShort update(Long userId, Long itemId, ItemDtoRequest itemDto);

    List<ItemDtoResponseLong> search(String text, int from, int size);

    CommentDtoResponse createComment(Long itemId, Long userId, CommentDtoRequest comment);
}
