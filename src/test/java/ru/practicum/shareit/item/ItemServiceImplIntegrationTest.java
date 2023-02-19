package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegrationTest {

    private final ItemServiceImpl itemService;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final ItemRequestRepository itemRequestRepository;

    long itemId;
    long userId;
    long requestId;
    long commentId;
    private  User user;
    private  Item item;
    private  Comment comment;
    private  Booking booking;
    private  ItemRequest itemRequest;
    private ItemDtoResponseLong itemDtoResponseLong;
    private ItemDtoRequest itemDtoRequest;



    @BeforeEach
    void beforeEach() {
        user = new User(null, "kot@tret.ru", "Kot");
        user = userRepository.save(user);
        userId = user.getId();

        item = new Item(null, "itemName", "description", user,
                true, null);
        item = itemRepository.save(item);
        itemId = item.getId();


        booking = new Booking(null, LocalDateTime.now(), LocalDateTime.now(), user, item, Status.APPROVED);
        booking = bookingRepository.save(booking);

        comment = new Comment(null, "comment", item, user, LocalDateTime.now());
        comment = commentRepository.save(comment);
        commentId = comment.getId();

        itemRequest = new ItemRequest(null, user, "description", LocalDateTime.now(),null);
        itemRequest = itemRequestRepository.save(itemRequest);
        requestId = itemRequest.getId();

    }

    @Test
    void getAll() {
        int from = 0;
        int size = 10;
        itemDtoResponseLong = ItemMapper.toDtoResponseLong(item);
        itemDtoResponseLong.setComments(List.of(CommentMapper.toDto(comment)));
        itemDtoResponseLong.setLastBooking(BookingMapper.toDtoShortOut(booking));

        List<ItemDtoResponseLong> actualDtoList = itemService.getAll(userId, from, size);
        List<ItemDtoResponseLong> expectedDtoList = List.of(itemDtoResponseLong);
        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void get() {
        itemDtoResponseLong = ItemMapper.toDtoResponseLong(item);
        itemDtoResponseLong.setComments(List.of(CommentMapper.toDto(comment)));
        itemDtoResponseLong.setLastBooking(BookingMapper.toDtoShortOut(booking));

        ItemDtoResponseLong expectedDto = itemDtoResponseLong;
        ItemDtoResponseLong actualDto = itemService.get(itemId, userId);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void create() {
        itemDtoRequest = new ItemDtoRequest(null, "new", "description",
                true, requestId);

        ItemDtoResponseShort saveItem = itemService.create(userId, itemDtoRequest);

        assertEquals(itemId + 1, saveItem.getId());
        assertEquals("new", saveItem.getName());
        assertEquals("description", saveItem.getDescription());
        assertEquals(requestId, saveItem.getRequestId());
        assertEquals(true, saveItem.getAvailable());

    }

    @Test
    void update() {
        itemDtoRequest = new ItemDtoRequest(null, "new", "description",
                false, null);

        ItemDtoResponseShort updateItem = itemService.update(userId, itemId, itemDtoRequest);

        assertEquals(itemId, updateItem.getId());
        assertEquals("new", updateItem.getName());
        assertEquals("description", updateItem.getDescription());
        assertEquals(false, updateItem.getAvailable());
    }

    @Test
    void search() {
        int from = 0;
        int size = 10;
        String text = "desc";
        itemDtoResponseLong = ItemMapper.toDtoResponseLong(item);

        List<ItemDtoResponseLong> actualDtoList = itemService.search(text, from, size);
        List<ItemDtoResponseLong> expectedDtoList = List.of(itemDtoResponseLong);
        assertEquals(expectedDtoList, actualDtoList);
    }

}