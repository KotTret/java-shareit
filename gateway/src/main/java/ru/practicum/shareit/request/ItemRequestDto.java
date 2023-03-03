package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.util.validation.Create;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "Описание не может быть пустым.", groups = Create.class)
    private String description;
}
