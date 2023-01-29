package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.util.validation.Update;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public  class BookingDto {

    @NotNull(groups = {Update.class})
    private Integer id;


    @NotNull(message = "Дата начала бронирования не может быть пустой")
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull(message = "Дата завершения бронирования не может быть пустой")
    @FutureOrPresent
    private LocalDateTime  end;

    @NotNull
    private Long itemId;

}
