package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.util.page.MyPageRequest;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;

    private final ItemRequestRepository repository;

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
        return ItemRequestMapper.toDtoList(repository.findAllByRequesterId(userId, pageable));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<ItemRequestDtoLong> getAll(Long userId, int from, int size) {
        checkUser(userId);
        MyPageRequest pageable = new MyPageRequest(from, size, Sort.by(Sort.Direction.DESC, "created"));
        log.info("Список запросов других пользоватлей для User с id: {}", userId);
        return  ItemRequestMapper.toDtoList(repository.findAllByUserId(userId, pageable));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public ItemRequestDtoLong getById(Long userId, Long requestId) {
        checkUser(userId);
        return ItemRequestMapper.toDtoLong(repository.findById(requestId).orElseThrow(() ->
                new ObjectNotFoundException("Request не найден, проверьте верно ли указан Id")));
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
}
