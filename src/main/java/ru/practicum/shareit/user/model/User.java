package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.util.validation.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {

    private Integer id;

    @NotNull
    @Email(message = "Формат электронной почты указан неверно.")
    private String email;

    @NotNull(groups = Create.class)
    private String name;

}
