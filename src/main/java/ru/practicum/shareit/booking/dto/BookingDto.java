package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;

    private Booker booker;

    private ItemBK item;

    @Data
    public static class Booker {
        private final long id;
        private final String name;
    }

    @Data
    public static class ItemBK {
        private final long id;
        private final String name;
    }
}
