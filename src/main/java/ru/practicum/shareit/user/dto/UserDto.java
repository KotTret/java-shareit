package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;
import ru.practicum.shareit.util.validation.myemail.MyEmail;
import ru.practicum.shareit.util.validation.notblanknull.NotBlankNull;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(groups = {Create.class})
    @NotBlankNull(groups = {Update.class})
    @MyEmail(groups = {Create.class, Update.class})
    private String email;

    @NotBlank(groups = {Create.class})
    @NotBlankNull(groups = {Update.class})
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(email, userDto.email) && Objects.equals(name, userDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name);
    }
}
