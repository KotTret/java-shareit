package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoShortRequest;
import ru.practicum.shareit.booking.dto.BookingDtoShortResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class BookingMapper {

    public static BookingDto toDto(Booking entity) {
        return BookingDto
                .builder()
                .id(entity.getId())
                .start(entity.getStart())
                .end(entity.getEnd())
                .status(entity.getStatus())
                .item(new BookingDto.ItemBK(entity.getItem().getId(), entity.getItem().getName()))
                .booker(new BookingDto.Booker(entity.getBooker().getId(), entity.getBooker().getName()))
                .build();
    }

    public static BookingDtoShortResponse toDtoShortOut(Booking entity) {
        return BookingDtoShortResponse
                .builder()
                .id(entity.getId())
                .start(entity.getStart())
                .end(entity.getEnd())
                .itemId(entity.getItem().getId())
                .bookerId(entity.getBooker().getId())
                .build();
    }

    public static Booking toEntity(BookingDtoShortRequest dto) {
        return Booking
                .builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .build();
    }

    public static List<BookingDto> toDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }
}
