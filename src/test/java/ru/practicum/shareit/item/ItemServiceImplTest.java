package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponseLong;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.page.MyPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  BookingRepository bookingRepository;
    @Mock
    private  CommentRepository commentRepository;
    @Mock
    private  ItemRequestRepository itemRequestRepository;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    private  User user;
    private  ItemDtoRequest itemDtoRequest;
    private  CommentDtoRequest commentDtoRequest;
    private  Item item;
    private  Comment comment;
    private  Booking booking;
    private  ItemRequest itemRequest;

    private ItemDtoResponseLong itemDtoResponseLong;

    @BeforeEach
    void create() {
        user = new User(1L, "kot@tret.ru", "Kot");

        item = new Item(1L, "itemName", "description", user,
                true, null);
        itemDtoRequest = new ItemDtoRequest(1L, "itemName", "description", true, 1L);

        itemRequest = new ItemRequest(1L, new User(), "description", null,null);

        booking = new Booking(1L, null, null, user, item, Status.WAITING);

        comment = new Comment(1L, "comment", item, user, null);
        commentDtoRequest = new CommentDtoRequest(1L, "comment");
    }


    @Test
    void getAll_whenOk_thenFoundItemDtoList() {
        long userId = 1L;
        MyPageRequest pageable = new MyPageRequest(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Item> page = new PageImpl<>(List.of(item));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwner(user, pageable))
                .thenReturn(page.toList());
        when(bookingRepository.findFirstByItemIdInAndStartLessThanEqual(any(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.findFirstByItemIdInAndStartAfter(any(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(commentRepository.findByItemIdIn(any(), any()))
                .thenReturn(List.of(comment));
        itemDtoResponseLong = ItemMapper.toDtoResponseLong(item);
        itemDtoResponseLong.setComments(List.of(CommentMapper.toDto(comment)));
        itemDtoResponseLong.setLastBooking(BookingMapper.toDtoShortOut(booking));
        itemDtoResponseLong.setNextBooking(BookingMapper.toDtoShortOut(booking));

        List<ItemDtoResponseLong> expectedDtoList = List.of(itemDtoResponseLong);
        List<ItemDtoResponseLong> actualDtoList = itemService.getAll(userId, 0, 10);

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void getAll_whenUserNotFound_thenObjectNotFoundExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        long userId = 999;

        assertThrows(ObjectNotFoundException.class, () -> itemService.getAll(userId, 0, 10));
    }

    @Test
    void get_whenItemNotFound_thenObjectNotFoundExceptionThrown() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        long itemId = 999;
        long userId = 1;

        assertThrows(ObjectNotFoundException.class, () -> itemService.get(itemId, userId));
    }

    @Test
    void get_whenOk_thenFoundItemDto() {
        long userId = 1L;
        long itemId = 1L;
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(any(), any()))
                .thenReturn(List.of(comment));
        when(bookingRepository.findFirstByItemIdAndStartLessThanEqual(any(),any(),any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.findFirstByItemIdAndStartAfter(any(),any(),any()))
                .thenReturn(Optional.of(booking));
        itemDtoResponseLong = ItemMapper.toDtoResponseLong(item);
        itemDtoResponseLong.setComments(List.of(CommentMapper.toDto(comment)));
        itemDtoResponseLong.setLastBooking(BookingMapper.toDtoShortOut(booking));
        itemDtoResponseLong.setNextBooking(BookingMapper.toDtoShortOut(booking));

        ItemDtoResponseLong expectedDto = itemDtoResponseLong;
        ItemDtoResponseLong actualDto = itemService.get(itemId, userId);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void get_whenUserIsNotOwner_thenFoundItemDtoWithoutBooking() {
        long userId = 1L;
        long itemId = 1L;
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(any(), any()))
                .thenReturn(List.of(comment));
        itemDtoResponseLong = ItemMapper.toDtoResponseLong(item);
        itemDtoResponseLong.setComments(List.of(CommentMapper.toDto(comment)));


        ItemDtoResponseLong expectedDto = itemDtoResponseLong;
        ItemDtoResponseLong actualDto = itemService.get(itemId, userId);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void create_whenOk_thenSaveItemWithItemRequest() {
        long userId = 1;
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));

        ItemDtoResponseShort expectedDto = new ItemDtoResponseShort(1L, "itemName",
                "description", true, 1L);
        ItemDtoResponseShort actualDto = itemService.create(userId, itemDtoRequest);

        assertEquals(expectedDto, actualDto);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item saveItem = itemArgumentCaptor.getValue();

        assertEquals(1L, saveItem.getId());
        assertEquals("itemName", saveItem.getName());
        assertEquals("description", saveItem.getDescription());
        assertEquals(user, saveItem.getOwner());
        assertEquals(itemRequest, saveItem.getItemRequest());
        assertEquals(true, saveItem.getAvailable());
    }

    @Test
    void create_whenOwnerNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 999;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> itemService.create(userId, itemDtoRequest));

        verify(itemRequestRepository, never()).findById(any());
        verify(itemRepository, never()).save(any());

    }

    @Test
    void update_whenOk_thenUpdateItem() {
        long userId = 1;
        long itemId = 1;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findByIdAndOwner(anyLong(), any()))
                .thenReturn(Optional.of(item));
        ItemDtoRequest updateItem = new ItemDtoRequest(1L, "itemName2",
                "description2", false, null);

        ItemDtoResponseShort expectedDto = new ItemDtoResponseShort(1L, "itemName2",
                "description2", false, null);
        ItemDtoResponseShort actualDto = itemService.update(userId, itemId, updateItem);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void update_whenItemNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 1;
        long itemId = 999;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findByIdAndOwner(anyLong(), any()))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> itemService.update(userId, itemId, itemDtoRequest));
    }

    @Test
    void update_whenOwnerNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 999;
        long itemId = 1;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> itemService.update(userId, itemId, itemDtoRequest));
    }

    @Test
    void search() {
        when(itemRepository.search(anyString(), any()))
                .thenReturn(List.of(item));

        List<ItemDtoResponseLong> expectedDtoList = List.of(ItemMapper.toDtoResponseLong(item));
        List<ItemDtoResponseLong> actualDtoList = itemService.search("text", 0, 10);

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void createComment_whenOk_thenSaveComment() {
        long userId = 1;
        long itemId = 1;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(new Booking()));
        when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDtoResponse actualDdo = itemService.createComment(itemId, userId, commentDtoRequest);

        assertEquals(1L, actualDdo.getId());
        assertEquals("comment", actualDdo.getText());
        assertEquals(1L, actualDdo.getAuthorId());
        assertEquals("Kot", actualDdo.getAuthorName());
        assertNull(actualDdo.getCreated());
        assertEquals("itemName", actualDdo.getItem().getName());
        assertEquals(1L, actualDdo.getId());

        verify(commentRepository).save(commentArgumentCaptor.capture());
        Comment saveComment = commentArgumentCaptor.getValue();

        assertEquals(1L, saveComment.getId());
        assertEquals("comment", saveComment.getText());
        assertEquals(user, saveComment.getAuthor());
        assertEquals(item, saveComment.getItem());

    }

    @Test
    void createComment_whenOwnerNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 999;
        long itemId = 1;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> itemService.createComment(itemId, userId, commentDtoRequest));

        verify(itemRepository, never()).findById(itemId);
        verify(bookingRepository, never()).findAllByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now());
        verify(commentRepository, never()).save(any());

    }

    @Test
    void createComment_whenItemNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 1;
        long itemId = 999;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> itemService.createComment(itemId, userId, commentDtoRequest));

        verify(bookingRepository, never()).findAllByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_whenBookingNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 1;
        long itemId = 999;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of());

        assertThrows(BadRequestException.class, () -> itemService.createComment(itemId, userId, commentDtoRequest));

        verify(commentRepository, never()).save(any());
    }
}