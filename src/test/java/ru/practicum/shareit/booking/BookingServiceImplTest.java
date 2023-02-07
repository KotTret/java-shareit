package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShortRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item;
    private Booking booking;

    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end = LocalDateTime.now();

    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;



    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "kot@tret.ru", "Kot");
        user2 = new User(2L, "user@tret.ru", "User");
        item = new Item(1L, "itemName", "description", user1,
                true, null);
        booking = new Booking(1L, start, end, user1, item, Status.WAITING);
    }

    @Test
    void get_whenOk_thenFoundBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.get(bookingId, userId);

        assertEquals(1L, result.getId());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
        assertEquals(Status.WAITING, result.getStatus());
        assertEquals(1L, result.getItem().getId());
        assertEquals("itemName", result.getItem().getName());
        assertEquals(1L, result.getBooker().getId());
        assertEquals("Kot", result.getBooker().getName());
    }

    @Test
    void get_whenUserIsNotBooker_thenObjectNotFoundExceptionThrown() {
        long bookingId = 1L;
        long userId = 999L;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(ObjectNotFoundException.class, () -> bookingService.get(bookingId, userId));

    }

    @Test
    void get_whenBookingIsNotFound_thenObjectNotFoundExceptionThrown() {
        long bookingId = 999L;
        long userId = 1L;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> bookingService.get(bookingId, userId));

    }

    @Test
    void findAllByOwnerId_whenStateIsAll() {
        long userId = 1L;
        State state = State.ALL;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItem_OwnerId(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByOwnerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByOwnerId_whenStateIsCURRENT() {
        long userId = 1L;
        State state = State.CURRENT;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItem_OwnerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByOwnerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByOwnerId_whenStateIsPAST() {
        long userId = 1L;
        State state = State.PAST;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItem_OwnerIdAndEndBefore(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByOwnerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByOwnerId_whenStateIsFUTURE() {
        long userId = 1L;
        State state = State.FUTURE;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItem_OwnerIdAndStartAfter(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByOwnerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByOwnerId_whenStateIsWAITING() {
        long userId = 1L;
        State state = State.WAITING;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItem_OwnerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByOwnerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByOwnerId_whenStateIsREJECTED() {
        long userId = 1L;
        State state = State.REJECTED;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItem_OwnerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByOwnerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByOwnerId_whenUserNotFound() {
        long userId = 999L;
        State state = State.ALL;
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () ->  bookingService.findAllByOwnerId(userId, state, 0, 10));
    }

    @Test
    void findAllByBookerId_whenStateIsAll() {
        long userId = 1L;
        State state = State.ALL;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerId(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByBookerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerId_whenStateIsCURRENT() {
        long userId = 1L;
        State state = State.CURRENT;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByBookerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerId_whenStateIsPAST() {
        long userId = 1L;
        State state = State.PAST;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndEndBefore(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByBookerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerId_whenStateIsFUTURE() {
        long userId = 1L;
        State state = State.FUTURE;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStartAfter(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByBookerId(
                userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerId_whenStateIsWAITING() {
        long userId = 1L;
        State state = State.WAITING;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByBookerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerId_whenStateIsREJECTED() {
        long userId = 1L;
        State state = State.REJECTED;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findAllByBookerId(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerId_whenUserNotFound() {
        long userId = 999L;
        State state = State.ALL;
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () ->  bookingService.findAllByBookerId(userId, state, 0, 10));
    }

    @Test
    void create_whenOk_thenSaveBooking() {
        long userId = 2L;
        long itemId = 1L;
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user2));
        BookingDtoShortRequest dto = new BookingDtoShortRequest(1L, start, end, itemId);

        BookingDto result = bookingService.create(dto, userId, itemId);

        assertEquals(1L, result.getId());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
        assertEquals(Status.WAITING, result.getStatus());
        assertEquals(1L, result.getItem().getId());
        assertEquals("itemName", result.getItem().getName());
        assertEquals(2L, result.getBooker().getId());
        assertEquals("User", result.getBooker().getName());

        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking save = bookingArgumentCaptor.getValue();

        assertEquals(1L, save.getId());
        assertEquals(start, save.getStart());
        assertEquals(end, save.getEnd());
        assertEquals(Status.WAITING, save.getStatus());
        assertEquals(user2, save.getBooker());
        assertEquals(item, save.getItem());
        assertEquals(Status.WAITING, save.getStatus());
    }

    @Test
    void create_whenUserIsNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 999L;
        long itemId = 1L;
        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());
        BookingDtoShortRequest dto = new BookingDtoShortRequest(1L, start, end, itemId);

        assertThrows(ObjectNotFoundException.class, () -> bookingService.create(dto, userId, itemId));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenItemIsNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 2L;
        long itemId = 999L;
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user2));
        BookingDtoShortRequest dto = new BookingDtoShortRequest(1L, start, end, itemId);

        assertThrows(ObjectNotFoundException.class, () -> bookingService.create(dto, userId, itemId));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenItemIsNotAvailable_thenBadRequestExceptionThrown() {
        long userId = 2L;
        long itemId = 1L;
        item.setAvailable(false);
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user2));
        BookingDtoShortRequest dto = new BookingDtoShortRequest(1L, start, end, itemId);

        assertThrows(BadRequestException.class, () -> bookingService.create(dto, userId, itemId));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenUserIsBooker_thenObjectNotFoundExceptionThrown() {
        long userId = 1L;
        long itemId = 1L;
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user1));
        BookingDtoShortRequest dto = new BookingDtoShortRequest(1L, start, end, itemId);

        assertThrows(ObjectNotFoundException.class, () -> bookingService.create(dto, userId, itemId));

        verify(bookingRepository, never()).save(any());
    }


    @Test
    void setStatus_whenOk_thenSetAPPROVEDStatus() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;
        booking.setStatus(Status.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertEquals(Status.APPROVED, bookingService.setStatus(userId, bookingId, approved).getStatus());
    }

    @Test
    void setStatus_whenOk_thenSetREJECTEDStatus() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = false;
        booking.setStatus(Status.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertEquals(Status.REJECTED, bookingService.setStatus(userId, bookingId, approved).getStatus());
    }

    @Test
    void setStatus_whenStatusIsAlreadyAPPROVED_thenBadRequestExceptionThrown() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(BadRequestException.class, () -> bookingService.setStatus(userId, bookingId, approved));
    }

    @Test
    void setStatus_whenStatusIsAlreadyREJECTED_thenBadRequestExceptionThrown() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;
        booking.setStatus(Status.CANCELED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(BadRequestException.class, () -> bookingService.setStatus(userId, bookingId, approved));
    }

    @Test
    void setStatus_whenUserIsNotOwner_thenObjectNotFoundExceptionThrown() {
        long userId = 999L;
        long bookingId = 1L;
        boolean approved = true;
        booking.setStatus(Status.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(ObjectNotFoundException.class, () -> bookingService.setStatus(userId, bookingId, approved));
    }
}