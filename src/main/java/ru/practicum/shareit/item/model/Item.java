package ru.practicum.shareit.item.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    private Integer id;


    private String name;


    private String description;


    private Integer ownerId;

    private Boolean available;

}
