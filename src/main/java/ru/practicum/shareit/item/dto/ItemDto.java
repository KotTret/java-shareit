package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;
import ru.practicum.shareit.util.validation.notblanknull.NotBlankNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlankNull(groups = {Update.class})
    @NotBlank(message = "Название не может быть пустым.", groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    @NotBlankNull(groups = {Update.class})
    @Size(max = 200, message = "Максимальная длина описания  — 200 символов.", groups = {Create.class, Update.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

    private BookingDtoShort lastBooking;

    private BookingDtoShort nextBooking;

    private List<CommentDto> comments;

}
