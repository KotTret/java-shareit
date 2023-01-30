package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.util.UtilMergeProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;


    @Override
    public List<ItemDto> getAll(Long userId) {
        List<Item> items = itemRepository.findAllByOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id")));
        List<ItemDto> itemsDto = items.stream().map(ItemMapper::toDto).collect(Collectors.toList());
        itemsDto.forEach(this::addLastAndNextBooking);
        log.info("Запрошено количество вещей: {}", items.size());
        return itemsDto;
    }

    @Override
    public Item get(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Item не найден, " +
                "проверьте верно ли указан Id"));
        log.info("Запрошена информация о Item:name - {}, id - {}", item.getName(), item.getId());
        return item;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Item create(Long userId, Item item) {
        setOwnerForItem(item, userId);
        itemRepository.save(item);
        log.info("Добавлен Item:name - {}, id - {}", item.getName(), item.getId());
        return item;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        checkItemForUser(userId, itemId);
        Item item = get(itemId);
        UtilMergeProperty.copyProperties(itemDto, item);
        log.info("Информация о Item обнолвена:name - {}, id - {}", item.getName(), item.getId());
        return item;
    }

    @Override
    public List<Item> search(String text) {
        log.info("Выполнен поиск Item по значению - {}", text);
        return itemRepository.search(text);
    }


    private void setOwnerForItem(Item item, Long userId) {
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id")));
    }

    private void checkItemForUser(Long userId, Long itemId) {
        itemRepository.findByIdAndOwner(itemId, userRepository.findById(userId)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id"))).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Item c id = %d не найден для пользователя c id = %d",
                        itemId, userId)));
    }

    private void addLastAndNextBooking(ItemDto dto) {
        Optional<Booking> lastBooking = bookingRepository.findByItemIdAndEndBeforeOrderByEndDesc(dto.getId()
                , LocalDateTime.now());
        lastBooking.ifPresent(booking -> dto.setLastBooking(BookingMapper.toDtoShort(booking)));

        Optional<Booking> nextBooking = bookingRepository.findByItemIdAndStartAfterOrderByStart(dto.getId()
                , LocalDateTime.now());
        nextBooking.ifPresent((booking -> dto.setNextBooking(BookingMapper.toDtoShort(booking))));
    }
}
