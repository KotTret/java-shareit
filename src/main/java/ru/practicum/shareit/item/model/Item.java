package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.valodation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Item {

    @NotNull(groups = Update.class)
    private Integer itemId;

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @NotBlank
    @Size(max = 200, message = "Максимальная длина описания  — 200 символов.")
    private String description;

    @NotBlank
    private Integer ownerId;

    private boolean isBooked;

    private int numberOfBookings;

    private Integer requestId;
}
