package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Booking {

    private Integer id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private User booker;

    private Item item;

    private Status status;
}
