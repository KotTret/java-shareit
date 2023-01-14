package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.valodation.Update;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @NotNull(groups = Update.class)
    private Integer idItem;

    private String name;

    private String description;

    private User owner;

    private boolean isBooked;

    private int numberOfBookings;
}
