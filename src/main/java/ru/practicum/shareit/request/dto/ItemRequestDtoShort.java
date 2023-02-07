package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.util.validation.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDtoShort {

    private Long id;
    @NotBlank(message = "Описание не может быть пустым.", groups = Create.class)
    private String description;
    private Long requesterId;
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDtoShort dtoShort = (ItemRequestDtoShort) o;
        return Objects.equals(id, dtoShort.id) && Objects.equals(description, dtoShort.description) && Objects.equals(requesterId, dtoShort.requesterId) && Objects.equals(created, dtoShort.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requesterId, created);
    }

    @Override
    public String toString() {
        return "ItemRequestDtoShort{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", requesterId=" + requesterId +
                ", created=" + created +
                '}';
    }
}
