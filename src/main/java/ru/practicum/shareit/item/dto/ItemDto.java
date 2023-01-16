package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Integer id;

    private String name;

    private String description;

    private Integer ownerId;

    private Boolean available;

    private int numberOfBookings;

    private Integer requestId;
}
