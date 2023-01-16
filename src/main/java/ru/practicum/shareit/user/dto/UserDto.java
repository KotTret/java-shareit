package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.util.validation.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;

    @NotNull(groups = Create.class)
    @Email(message = "Формат электронной почты указан неверно.")
    private String email;

    @NotNull(groups = Create.class)
    private String name;
}
