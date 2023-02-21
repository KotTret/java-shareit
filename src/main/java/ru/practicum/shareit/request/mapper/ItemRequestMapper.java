package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toEntity(ItemRequestDtoShort dto) {
        return ItemRequest
                .builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .created(dto.getCreated())
                .build();
    }

    public static ItemRequestDtoShort toDtoShort(ItemRequest entity) {
        return ItemRequestDtoShort
                .builder()
                .id(entity.getId())
                .requesterId(entity.getRequester().getId())
                .created(entity.getCreated())
                .description(entity.getDescription())
                .build();
    }

    public static ItemRequestDtoLong toDtoLong(ItemRequest entity) {
        return ItemRequestDtoLong
                .builder()
                .id(entity.getId())
                .requesterId(entity.getRequester().getId())
                .created(entity.getCreated())
                .description(entity.getDescription())
                .build();
    }

    public static List<ItemRequestDtoLong> toDtoList(List<ItemRequest> itemRequests) {
        return itemRequests.stream().map(ItemRequestMapper::toDtoLong).collect(Collectors.toList());
    }
}
