package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    private int numberOfBookings;

    private Integer requestId;
}
