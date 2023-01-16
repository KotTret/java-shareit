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

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @NotBlank
    @Size(max = 200, message = "Максимальная длина описания  — 200 символов.")
    private String description;


    private Integer ownerId;
    @NotNull
    private Boolean available;

    private int numberOfBookings;

    private Integer requestId;
}
