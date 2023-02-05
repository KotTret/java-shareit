package ru.practicum.shareit.item.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDtoRequest {

    private Long id;
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;

}
