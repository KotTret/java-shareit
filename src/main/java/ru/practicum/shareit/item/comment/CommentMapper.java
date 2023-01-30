package ru.practicum.shareit.item.comment;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentMapper {

    private static ModelMapper mapper = new ModelMapper();


    public static Comment toEntity(CommentDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Comment.class);
    }

    public static CommentDto toDto(Comment entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, CommentDto.class);
    }

}
