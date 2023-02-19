package ru.practicum.shareit.item;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private  User user;
    private  Item item1;
    private  Item item2;
    private  ItemRequest itemRequest;


    @BeforeEach
    void add() {
        user = new User(null, "kot@tret.ru", "Kot");
        userRepository.save(user);

        item1 = new Item(null, "item Name", "description", user, true, null);
        itemRepository.save(item1);
        item2 = new Item(null, "item Name2", "description2", user,
                true, null);
        itemRepository.save(item2);

        itemRequest = new ItemRequest(null, user, "description", LocalDateTime.now(), List.of(item1));
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void search() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Item> expected = List.of(item1, item2);

        List<Item> actual1 = itemRepository.search("item", pageable);
        assertEquals(expected, actual1);
        assertEquals(2, actual1.size());

        List<Item> actual2 = itemRepository.search("ITEM", pageable);
        assertEquals(expected, actual2);
        assertEquals(2, actual2.size());


        List<Item> actual3 = itemRepository.search("descrip", pageable);
        assertEquals(expected, actual3);
        assertEquals(2, actual3.size());

        List<Item> actual4 = itemRepository.search("ReturnEmpty", pageable);
        assertEquals(List.of(), actual4);
    }
}