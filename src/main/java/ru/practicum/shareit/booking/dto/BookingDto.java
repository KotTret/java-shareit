package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BookingDto {

    private Integer idBooking;

    private LocalDate startDate;

    private Duration duration;

    private LocalDate endDate;

    private Integer ownerId;

    private Integer tenantId;

    private Integer itemId;

    private boolean isConfirmFromOwner;
}
