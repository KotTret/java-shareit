package ru.practicum.shareit.item.comment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDtoResponse that = (CommentDtoResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(item, that.item) && Objects.equals(authorId, that.authorId) && Objects.equals(authorName, that.authorName) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, item, authorId, authorName, created);
    }

    @Override
    public String toString() {
        return "CommentDtoResponse{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", item=" + item +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", created=" + created +
                '}';
    }
}
