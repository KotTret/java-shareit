package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingDtoShortResponse {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDtoShortResponse that = (BookingDtoShortResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(itemId, that.itemId) && Objects.equals(bookerId, that.bookerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, itemId, bookerId);
    }

    @Override
    public String toString() {
        return "BookingDtoShortResponse{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", itemId=" + itemId +
                ", bookerId=" + bookerId +
                '}';
    }
}
