package ru.practicum.shareit.item.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDtoResponse {

    private Long id;
    private String text;
    private ItemCom item;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;

    @Data
    public static class ItemCom {
        private final long id;
        private final String name;
    }

}
