package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShortRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.util.page.MyPageRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public BookingDto get(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Бронь не найдена, проверьте верно ли указан Id"));
        if (booking.getBooker().getId().equals(userId)
                || booking.getItem().getOwner().getId().equals(userId)) {
            log.info("Запрошена информация о брони с id: {}", id);
            return BookingMapper.toDto(booking);
        } else {
            throw new ObjectNotFoundException("Пользователь не является арендатором или владельцем Item");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<BookingDto> findAllByOwnerId(Long userId, State state, int from, int size) {
        if (userRepository.existsById(userId)) {
            log.info("Запрошена информация о бронированиях для всех вещей пользователя с id: {}, по статусу: {}",
                    userId, state);
            MyPageRequest pageable = new MyPageRequest(from, size, Sort.by(Sort.Direction.DESC, "start"));
            switch (state) {
                case ALL:
                    return BookingMapper.toDtoList(bookingRepository.findAllByItem_OwnerId(userId,
                            pageable));
                case CURRENT:
                    return BookingMapper.toDtoList(bookingRepository
                            .findAllByItem_OwnerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                                    LocalDateTime.now(), pageable));
                case PAST:
                    return BookingMapper.toDtoList(bookingRepository
                            .findAllByItem_OwnerIdAndEndBefore(userId, LocalDateTime.now(),
                                    pageable));
                case FUTURE:
                    return BookingMapper.toDtoList(bookingRepository
                            .findAllByItem_OwnerIdAndStartAfter(userId, LocalDateTime.now(),
                                    pageable));
                case WAITING:
                    return BookingMapper.toDtoList(bookingRepository
                            .findAllByItem_OwnerIdAndStatus(userId, Status.WAITING,
                                    pageable));
                case REJECTED:
                    return BookingMapper.toDtoList(bookingRepository
                            .findAllByItem_OwnerIdAndStatus(userId, Status.REJECTED,
                                    pageable));
                default:
                    throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<BookingDto> findAllByBookerId(Long userId, State state, int from, int size) {
        if (userRepository.existsById(userId)) {
            log.info("Запрошена информация о всех бронированиях пользователя с id: {}, по статусу: {}", userId, state);
            MyPageRequest pageable = new MyPageRequest(from, size, Sort.by(Sort.Direction.DESC, "start"));
            switch (state) {
                case ALL:
                    return BookingMapper.toDtoList(bookingRepository.findAllByBookerId(userId,
                            pageable));
                case CURRENT:
                    return BookingMapper.toDtoList(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId,
                            LocalDateTime.now(), LocalDateTime.now(), pageable));
                case PAST:
                    return BookingMapper.toDtoList(bookingRepository.findAllByBookerIdAndEndBefore(userId,
                            LocalDateTime.now(),pageable));
                case FUTURE:
                    return BookingMapper.toDtoList(bookingRepository.findAllByBookerIdAndStartAfter(userId,
                            LocalDateTime.now(), pageable));
                case WAITING:
                    return BookingMapper.toDtoList(bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING,
                            pageable));
                case REJECTED:
                    return BookingMapper.toDtoList(bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED,
                            pageable));
                default:
                    throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingDto create(BookingDtoShortRequest bookingDto, Long userId, Long itemId) {
        Booking booking = BookingMapper.toEntity(bookingDto);
        setUserAndItemForBooking(booking, userId, itemId);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        log.info("Добавлено новое бронирование:для Item c id - {}, от User c id - {}", itemId, userId);
        return BookingMapper.toDto(booking);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingDto setStatus(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Бронь не найдена, проверьте верно ли указан Id"));
        if (booking.getItem().getOwner().getId().equals(userId)) {
            Status status = booking.getStatus();
            switch (status) {
                case APPROVED:
                    throw new BadRequestException("Статус уже был принят как 'APPROVED'");
                case CANCELED:
                    throw new BadRequestException("Статус уже был принят как 'CANCELED'");
                default:
                    if (approved) {
                        booking.setStatus(Status.APPROVED);
                    } else {
                        booking.setStatus(Status.REJECTED);
                    }
            }
        } else {
            throw new ObjectNotFoundException("Пользователь не является владельцем Item");
        }
        log.info("Статус для брони c id - {}, изменён на - {}", bookingId, booking.getStatus());
        return BookingMapper.toDto(booking);
    }

    private void setUserAndItemForBooking(Booking booking, Long userId, Long itemId) {
        booking.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id")));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Item не найден, проверьте верно ли указан Id"));
        if (item.getAvailable().equals(Boolean.FALSE)) {
            throw new BadRequestException("Item is not Available");
        }

        if (userId.equals(item.getOwner().getId())) {
            throw new ObjectNotFoundException("Предмет не может быть забронирован у владельца Item");
        }
        booking.setItem(item);
    }

}
