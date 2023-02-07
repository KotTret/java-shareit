package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
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
class ItemRequestServiceImplIntegrationTest {

    private final ItemRequestServiceImpl service;

    private final UserRepository userRepository;

    private final ItemRequestRepository repository;

    User user;
    ItemRequest itemRequest;
    ItemRequestDtoShort itemRequestDtoShort;
    ItemRequestDtoLong itemRequestDtoLong;
    long userId;
    long requestId;
    LocalDateTime created = LocalDateTime.now();

    @BeforeEach
    void beforeEach() {
        user = new User(null, "kot@tret.ru", "Kot");
        user = userRepository.save(user);
        userId = user.getId();

        itemRequest = new ItemRequest(null, user, "desc", created, List.of());
        itemRequest = repository.save(itemRequest);
        requestId = itemRequest.getId();

        itemRequestDtoShort = new ItemRequestDtoShort(null, "description", userId, created);
        itemRequestDtoLong = new ItemRequestDtoLong(requestId, "desc", userId, created, List.of());

    }

    @Test
    void create() {
        ItemRequestDtoShort result = service.create(itemRequestDtoShort);

        assertEquals(requestId + 1, result.getId());
        assertEquals(userId, result.getRequesterId());
        assertEquals("description", result.getDescription());
        assertEquals(created, result.getCreated());
    }

    @Test
    void getAllByRequester() {
        int from = 0;
        int size = 10;
        List<ItemRequestDtoLong> result = service.getAllByRequester(userId, from, size);

        assertEquals(List.of(itemRequestDtoLong), result);
    }

    @Test
    void getAll() {
        int from = 0;
        int size = 10;
        List<ItemRequestDtoLong> result = service.getAll(userId, from, size);

        assertEquals(List.of(), result);
    }

    @Test
    void getById() {
        ItemRequestDtoLong result = service.getById(userId, requestId);

        assertEquals(requestId, result.getId());
        assertEquals(userId, result.getRequesterId());
        assertEquals("desc", result.getDescription());
        assertEquals(created, result.getCreated());
    }
}