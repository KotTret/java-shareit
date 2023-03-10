package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponseLong;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {


    public static Item toEntity(ItemDtoRequest dto) {
        return Item
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public static ItemDtoResponseLong toDtoResponseLong(Item entity) {
        return ItemDtoResponseLong
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .available(entity.getAvailable())
                .build();
    }

    public static ItemDtoResponseShort toDtoResponseShort(Item entity) {
        return ItemDtoResponseShort
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .available(entity.getAvailable())
                .requestId(entity.getItemRequest() == null ? null : entity.getItemRequest().getId())
                .build();
    }

    public static List<ItemDtoResponseLong> toDtoLongList(List<Item> items) {
        return items.stream().map(ItemMapper::toDtoResponseLong).collect(Collectors.toList());
    }

    public static List<ItemDtoResponseShort> toDtoShortList(List<Item> items) {
        return items.stream().map(ItemMapper::toDtoResponseShort).collect(Collectors.toList());
    }

}
