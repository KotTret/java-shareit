package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.util.validation.startbeforeanddate.StartBeforeEndDateValid;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@StartBeforeEndDateValid
public class BookingDtoShortRequest {

    private Long id;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    @NotNull
    private Long itemId;

}
