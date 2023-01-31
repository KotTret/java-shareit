package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoResponseShort {

    private Long id;
    private String name;
    private String description;
    private Boolean available;

}
