package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ModelMapper mapper;


    public Booking toEntity(BookingDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Booking.class);
    }

    public BookingDto toDto(Booking entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, BookingDto.class);
    }
}
