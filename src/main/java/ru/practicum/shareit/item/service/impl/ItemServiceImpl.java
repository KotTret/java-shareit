package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoShortResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponseLong;
import ru.practicum.shareit.item.dto.ItemDtoResponseShort;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.UtilMergeProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<ItemDtoResponseLong> getAll(Long userId) {
        List<Item> items = itemRepository.findAllByOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id")));
        List<ItemDtoResponseLong> itemsDto = ItemMapper.toDtoList(items);
        addLastAndNextBooking(itemsDto);
        addComments(itemsDto);
        log.info("Запрошено количество вещей: {}", items.size());
        return itemsDto;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public ItemDtoResponseLong get(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Item не найден, " +
                "проверьте верно ли указан Id"));
        ItemDtoResponseLong dto = ItemMapper.toDtoResponseLong(item);
        addComments(dto);
        if (item.getOwner().getId().equals(userId)) {
            addLastAndNextBooking(dto);
        }
        log.info("Запрошена информация о Item:name - {}, id - {}", item.getName(), item.getId());
        return dto;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ItemDtoResponseShort create(Long userId, ItemDtoRequest itemDto) {
        Item item = ItemMapper.toEntity(itemDto);
        setOwnerForItem(item, userId);
        itemRepository.save(item);
        log.info("Добавлен Item:name - {}, id - {}", item.getName(), item.getId());
        return ItemMapper.toDtoResponseShort(item);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ItemDtoResponseShort update(Long userId, Long itemId, ItemDtoRequest itemDto) {
        checkItemForUser(userId, itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Item не найден, " +
                "проверьте верно ли указан Id"));
        UtilMergeProperty.copyProperties(itemDto, item);
        log.info("Информация о Item обнолвена:name - {}, id - {}", item.getName(), item.getId());
        return ItemMapper.toDtoResponseShort(item);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<ItemDtoResponseLong> search(String text) {
        log.info("Выполнен поиск Item по значению - {}", text);
        return ItemMapper.toDtoList(itemRepository.search(text));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public CommentDtoResponse createComment(Long itemId, Long userId, CommentDtoRequest commentDtoRequest) {
        Comment comment = CommentMapper.toEntity(commentDtoRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Item не найден, " +
                "проверьте верно ли указан Id"));
        if (!bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())
                .isEmpty()) {
            comment.setItem(item);
            comment.setAuthor(user);
            comment.setCreated(LocalDateTime.now());
            return CommentMapper.toDto(commentRepository.save(comment));
        } else {
            throw new BadRequestException(String.format("Невозможно пока оставить комментарий!!! " +
                    "Пользователь с id=%d не бронировал или не завершил бронь " +
                    "с предметом с id=%d", userId, itemId));
        }
    }


    private void setOwnerForItem(Item item, Long userId) {
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id")));
    }

    private void checkItemForUser(Long userId, Long itemId) {
        itemRepository.findByIdAndOwner(itemId, userRepository.findById(userId)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Пользователь не найден," +
                                " проверьте верно ли указан Id"))).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Item c id = %d не найден для пользователя c id = %d",
                        itemId, userId)));
    }

    private void addLastAndNextBooking(ItemDtoResponseLong dto) {
        Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStartLessThanEqual(dto.getId(),
                LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
        lastBooking.ifPresent(booking -> dto.setLastBooking(BookingMapper.toDtoShortOut(booking)));

        Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStartAfter(dto.getId(),
                LocalDateTime.now(), Sort.by(Sort.Direction.ASC, "start"));
        nextBooking.ifPresent((booking -> dto.setNextBooking(BookingMapper.toDtoShortOut(booking))));
    }

    private void addLastAndNextBooking(List<ItemDtoResponseLong> items) {
        List<Long> idItems = items.stream().map(ItemDtoResponseLong::getId).collect(Collectors.toList());
        Map<Long, BookingDtoShortResponse> lastBookings = bookingRepository.findFirstByItemIdInAndStartLessThanEqual(idItems,
                        LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .map(BookingMapper::toDtoShortOut)
                .collect(Collectors.toMap(BookingDtoShortResponse::getItemId, Function.identity()));
        items.forEach(i -> i.setLastBooking(lastBookings.get(i.getId())));
        Map<Long, BookingDtoShortResponse> nextBookings = bookingRepository.findFirstByItemIdInAndStartAfter(idItems,
                        LocalDateTime.now(),
                        Sort.by(Sort.Direction.ASC, "start"))
                .stream()
                .map(BookingMapper::toDtoShortOut)
                .collect(Collectors.toMap(BookingDtoShortResponse::getItemId, Function.identity()));
        items.forEach(i -> i.setNextBooking(nextBookings.get(i.getId())));
    }

    private void addComments(List<ItemDtoResponseLong> items) {
        List<Long> idItems = items.stream().map(ItemDtoResponseLong::getId).collect(Collectors.toList());
        Map<Long, List<CommentDtoResponse>> comments = commentRepository.findByItemIdIn(idItems,
                        Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(c -> c.getItem().getId()));
        items.forEach(i -> i.setComments(comments.get(i.getId())));
    }

    private void addComments(ItemDtoResponseLong itemDto) {
        List<Comment> comments = commentRepository.findByItemId(itemDto.getId(),
                Sort.by(Sort.Direction.DESC, "created"));
        List<CommentDtoResponse> commentsDto = comments.stream().map(CommentMapper::toDto).collect(Collectors.toList());
        itemDto.setComments(commentsDto);
    }
}
