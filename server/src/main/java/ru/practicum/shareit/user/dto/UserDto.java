package ru.practicum.shareit.user.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
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
