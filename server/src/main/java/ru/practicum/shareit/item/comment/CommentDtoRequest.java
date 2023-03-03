package ru.practicum.shareit.item.comment;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDtoRequest {

    private Long id;
    private String text;

}
