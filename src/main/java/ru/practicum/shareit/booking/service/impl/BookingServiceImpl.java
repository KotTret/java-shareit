package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking get(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Бронь не найдена, проверьте верно ли указан Id"));
        if (booking.getBooker().getId().equals(userId)
                || booking.getItem().getOwner().getId().equals(userId)) {
            log.info("Запрошена информация о брони с id: {}", id);
           return booking;
        } else {
            throw new ObjectNotFoundException("Пользователь не является арендатором или владельцем Item");
        }
    }

    @Override
    public List<Booking> findAllByOwnerId(Long userId, String state) {
        if(userRepository.existsById(userId)) {
            log.info("Запрошена информация о бронированиях для всех вещей пользователя с id: {}, по статусу: {}" , userId, state);
            try {
                State.valueOf(state);
            } catch (RuntimeException e) {
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findAllByItem_OwnerIdOrderByStartDesc(userId);
                case CURRENT:
                    return bookingRepository.findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now());
                case PAST:
                    return bookingRepository.findAllByItem_OwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findAllByItem_OwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                case WAITING:
                    return bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                case REJECTED:
                    return bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                default:
                    throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new  ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id");
        }
    }

    @Override
    public List<Booking> findAllByBookerId(Long userId, String state) {
        if(userRepository.existsById(userId)) {
            log.info("Запрошена информация о всех бронированиях пользователя с id: {}, по статусу: {}" , userId, state);
            try {
                State.valueOf(state);
            } catch (RuntimeException e) {
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                case CURRENT:
                    return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now());
                case PAST:
                    return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                case WAITING:
                    return  bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                case REJECTED:
                    return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                default:
                    throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new  ObjectNotFoundException("Пользователь не найден, проверьте верно ли указан Id");
        }
    }

    @Override
    @Transactional
    public Booking create(Booking booking, Long userId, Long itemId) {
        validate(booking, userId);
        setUserAndItemForBooking(booking, userId, itemId);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        log.info("Добавлено новое бронирование:для Item c id - {}, от User c id - {}", itemId, userId);
        return booking;
    }


    @Override
    @Transactional
    public Booking setStatus(Long userId, Long bookingId, boolean approved) {
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
        return booking;
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
    private void validate(Booking booking, Long userId) {
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Бронь в прошедшем времени");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BadRequestException("Завершение брони раньше ее регистрации");
        }

    }

}
