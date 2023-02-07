package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository repository;

    @Captor
    private ArgumentCaptor<ItemRequest> itemRequestArgumentCaptor;

    @Test
    void create_whenOk_thenSaveItemRequestWith() {
        long userId = 1L;
        User user = new User(userId, "kot@tret.ru", "Kot");
        ItemRequestDtoShort dtoShort = new ItemRequestDtoShort(1L, "desc", userId, null);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ItemRequestDtoShort expectedDto = new ItemRequestDtoShort(1L, "desc", userId, null);
        ItemRequestDtoShort actualDto = service.create(dtoShort);

        assertEquals(expectedDto, actualDto);

        verify(repository).save(itemRequestArgumentCaptor.capture());
        ItemRequest save = itemRequestArgumentCaptor.getValue();

        assertEquals(1L, save.getId());
        assertEquals("desc", save.getDescription());
        assertEquals(user, save.getRequester());
    }

    @Test
    void create_whenUserNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 999L;
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        ItemRequestDtoShort dtoShort = new ItemRequestDtoShort(1L, "desc", userId, null);

        assertThrows(ObjectNotFoundException.class, () -> service.create(dtoShort));

        verify(repository, never()).save(any());
    }

    @Test
    void getAllByRequester_whenOk_thenFoundItemRequestDtoList() {
        long userId = 1L;
        User user = new User(userId, "kot@tret.ru", "Kot");
        ItemRequest itemRequest = new ItemRequest(1L, user, "desc", null, List.of());
        ItemRequestDtoLong itemRequestDtoLong = new ItemRequestDtoLong(1L, "desc", userId, null, List.of());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(repository.findAllByRequesterId(anyLong(), any())).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoLong> expectedDtoList = List.of(itemRequestDtoLong);
        List<ItemRequestDtoLong> actualDtoList = service.getAllByRequester(userId, 0, 10);

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void getAll_whenOk_thenFoundItemRequestDtoList() {
        long userId = 1L;
        User user = new User(userId, "kot@tret.ru", "Kot");
        ItemRequest itemRequest = new ItemRequest(1L, user, "desc", null, List.of());
        ItemRequestDtoLong itemRequestDtoLong = new ItemRequestDtoLong(1L, "desc", userId, null, List.of());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(repository.findAllByUserId(anyLong(), any())).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoLong> expectedDtoList = List.of(itemRequestDtoLong);
        List<ItemRequestDtoLong> actualDtoList = service.getAll(userId, 0, 10);

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void getAll__whenUserNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 999L;

        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.getAll(userId, 0, 10));

        verify(repository, never()).findAllByUserId(anyLong(), any());
    }

    @Test
    void getById_whenOk_thenFoundItemRequestDto() {
        long userId = 1L;
        long requestId = 1L;
        User user = new User(userId, "kot@tret.ru", "Kot");
        ItemRequest itemRequest = new ItemRequest(1L, user, "desc", null, List.of());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        ItemRequestDtoLong expected = new ItemRequestDtoLong(1L, "desc", userId, null, List.of());
        ItemRequestDtoLong actual = service.getById(userId, requestId);

        assertEquals(expected, actual);
    }

    @Test
    void getById_whenUserNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 999L;
        long requestId = 1L;
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.getById(userId, requestId));

        verify(repository, never()).findById(anyLong());
    }

    @Test
    void getById_whenRequestNotFound_thenObjectNotFoundExceptionThrown() {
        long userId = 1L;
        long requestId = 999L;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.getById(userId, requestId));
    }
}