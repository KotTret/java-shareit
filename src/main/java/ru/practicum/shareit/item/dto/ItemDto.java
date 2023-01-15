package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class ItemDto {

    private Integer itemId;

    private String name;

    private String description;

    private Integer ownerId;

    private boolean isBooked;

    private int numberOfBookings;

    private Integer requestId;
}


