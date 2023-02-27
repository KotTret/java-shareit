package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoShort create(ItemRequestDtoShort itemRequestDto);

    List<ItemRequestDtoLong> getAllByRequester(Long userId, int from, int size);

    List<ItemRequestDtoLong> getAll(Long userId, int from, int size);

    ItemRequestDtoLong getById(Long userId, Long requestId);
}
