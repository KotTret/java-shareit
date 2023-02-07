package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.util.validation.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDtoLong {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым.", groups = Create.class)
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<ItemDtoResponseShort> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDtoLong that = (ItemRequestDtoLong) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(requesterId, that.requesterId) && Objects.equals(created, that.created) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requesterId, created, items);
    }
}
