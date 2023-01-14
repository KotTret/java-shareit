package ru.practicum.shareit.booking;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    private Integer idBooking;

    private LocalDate startDate;

    private Duration duration;

    private LocalDate endDate;

    private Integer ownerId;

    private Integer tenantId;

    private Integer itemId;

    private boolean isConfirmFromOwner;
}
