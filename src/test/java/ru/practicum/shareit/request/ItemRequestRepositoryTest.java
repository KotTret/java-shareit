package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    ItemRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private  User user1;
    private  User user2;
    private  Item item;
    private  ItemRequest itemRequest;

    @BeforeEach
    void create() {
        user1 = new User(null, "kot@tret.ru", "Kot");
        user2 = new User(null, "user@tret.ru", "User");
        item = new Item(null, "item Name", "description", user1, true, null);
        itemRequest = new ItemRequest(null, user1, "description", LocalDateTime.now(), List.of(item));
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        item = itemRepository.save(item);
        itemRequest = requestRepository.save(itemRequest);
    }

    @Test
    void findAllByRequesterId() {
    }

    @Test
    void findAllByUserId_whenUserIsNotRequester_thenReturnList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created"));

        List<ItemRequest> actual = requestRepository.findAllByUserId(user2.getId(), pageable);

        assertEquals(1, actual.size());
    }

    @Test
    void findAllByUserId_whenUserIsRequester_thenReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created"));

        List<ItemRequest> actual = requestRepository.findAllByUserId(user1.getId(), pageable);

        assertTrue(actual.isEmpty());
    }
}