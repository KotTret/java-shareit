package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public  class BookingDtoShort {

    private Long id;

    @NotNull(message = "Дата начала бронирования не может быть пустой")
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull(message = "Дата завершения бронирования не может быть пустой")
    @FutureOrPresent
    private LocalDateTime  end;

    @NotNull
    private Long itemId;

    private Long bookerId;

}
