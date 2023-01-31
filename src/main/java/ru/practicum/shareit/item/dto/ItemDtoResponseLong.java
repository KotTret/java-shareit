package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoShortResponse;
import ru.practicum.shareit.item.comment.CommentDtoResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoResponseLong {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoShortResponse lastBooking;
    private BookingDtoShortResponse nextBooking;
    private List<CommentDtoResponse> comments;

}
