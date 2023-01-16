package ru.practicum.shareit.booking;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Booking {

    private Integer bookingId;

    private LocalDateTime startDate;

    private Duration duration;

    private LocalDateTime endDate;

    private Integer ownerId;

    private Integer bookerId;

    private Integer itemId;

    private Status status;
}
