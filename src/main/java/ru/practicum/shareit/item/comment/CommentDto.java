package ru.practicum.shareit.item.comment;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
    private Item item;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;
}
