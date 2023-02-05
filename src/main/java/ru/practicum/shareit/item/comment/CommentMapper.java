package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {


    public static Comment toEntity(CommentDtoRequest dto) {
        return Comment
                .builder()
                .text(dto.getText())
                .build();
    }

    public static CommentDtoResponse toDto(Comment entity) {
        return CommentDtoResponse
                .builder()
                .id(entity.getId())
                .text(entity.getText())
                .item(new CommentDtoResponse.ItemCom(entity.getItem().getId(), entity.getItem().getName()))
                .authorName(entity.getAuthor().getName())
                .authorId(entity.getAuthor().getId())
                .created(entity.getCreated())
                .build();
    }
}
