package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;
import ru.practicum.shareit.util.validation.notblanknull.NotBlankNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoRequest {

    private Long id;
    @NotBlankNull(groups = {Update.class})
    @NotBlank(message = "Название не может быть пустым.", groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    @NotBlankNull(groups = {Update.class})
    @Size(max = 200, message = "Максимальная длина описания  — 200 символов.", groups = {Create.class, Update.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

}
