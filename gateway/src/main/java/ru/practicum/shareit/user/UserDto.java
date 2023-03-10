package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;
import ru.practicum.shareit.util.validation.myemail.MyEmail;
import ru.practicum.shareit.util.validation.notblanknull.NotBlankNull;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    @NotBlank(groups = {Create.class})
    @NotBlankNull(groups = {Update.class})
    @MyEmail(groups = {Create.class, Update.class})
    private String email;

    @NotBlank(groups = {Create.class})
    @NotBlankNull(groups = {Update.class})
    private String name;

}
