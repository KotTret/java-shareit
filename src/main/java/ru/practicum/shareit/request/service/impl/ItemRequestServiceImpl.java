package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.util.page.MyPageRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;

    private final ItemRequestRepository repository;

    private final ItemRepository itemRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ItemRequestDtoShort create(ItemRequestDtoShort itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toEntity(itemRequestDto);
        setUser(itemRequest, itemRequestDto.getRequesterId());
        repository.save(itemRequest);
        log.info("Запрос с id = '{}' добавлен в список", itemRequest.getId());
        return ItemRequestMapper.toDtoShort(itemRequest);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<ItemRequestDtoLong> getAllByRequester(Long userId, int from, int size) {
        checkUser(userId);
        MyPageRequest pageable = new MyPageRequest(from, size, Sort.by(Sort.Direction.DESC, "created"));
        log.info("Список запросов пользователя с id: {}", userId);
        List<ItemRequestDtoLong> itemRequestDtoLongs = ItemRequestMapper
                .toDtoList(repository.findAllByRequesterId(userId, pageable));
        addItemsForRequests(itemRequestDtoLongs);
        return itemRequestDtoLongs;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<ItemRequestDtoLong> getAll(Long userId, int from, int size) {
        checkUser(userId);
        MyPageRequest pageable = new MyPageRequest(from, size, Sort.by(Sort.Direction.DESC, "created"));
        log.info("Список запросов других пользоватлей для User с id: {}", userId);
        List<ItemRequestDtoLong> itemRequestDtoLongs = ItemRequestMapper
                .toDtoList(repository.findAllByUserId(userId, pageable));
        addItemsForRequests(itemRequestDtoLongs);
        return itemRequestDtoLongs;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public ItemRequestDtoLong getById(Long userId, Long requestId) {
        checkUser(userId);
        ItemRequestDtoLong itemRequestDtoLong = ItemRequestMapper.toDtoLong(repository.findById(requestId).orElseThrow(() ->
                new ObjectNotFoundException("Request не найден, проверьте верно ли указан Id")));
        addItemsForRequests(itemRequestDtoLong);
        return itemRequestDtoLong;
    }

    private void setUser(ItemRequest itemRequest, Long requesterId) {
        itemRequest.setRequester(userRepository.findById(requesterId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден," +
                        " проверьте верно ли указан Id")));
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден," +
                    " проверьте верно ли указан Id");
        }
    }

    private void addItemsForRequests(List<ItemRequestDtoLong> requests) {
        List<Long> idRequests = requests.stream().map(ItemRequestDtoLong::getId).collect(Collectors.toList());
        Map<Long, List<ItemDtoResponseShort>> items = itemRepository.findByItemRequestIdIn(idRequests,
                        Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(ItemMapper::toDtoResponseShort)
                .collect(Collectors.groupingBy(ItemDtoResponseShort::getRequestId));
        requests.forEach(i -> i.setItems(items.getOrDefault(i.getId(), List.of())));
    }

    private void addItemsForRequests(ItemRequestDtoLong request) {
        List<Item> items = itemRepository.findByItemRequestId(request.getId(),
                Sort.by(Sort.Direction.DESC, "id"));
        List<ItemDtoResponseShort> itemDtoResponseShorts = items.stream()
                .map(ItemMapper::toDtoResponseShort).collect(Collectors.toList());
        request.setItems(itemDtoResponseShorts);
    }

}
