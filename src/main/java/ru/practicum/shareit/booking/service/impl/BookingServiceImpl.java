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
            throw new ValidationException("Пользователь не является арендатором или владельцем Item");
        }
    }


    @Override
    public List<Booking> findAllByBookerId(Long userId, State state) {
        if(userRepository.existsById(userId)) {
            LocalDateTime
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
        if (booking.getBooker().getId().equals(userId)) {
            Status status = booking.getStatus();
            switch (status) {
                case APPROVED:
                    throw new ValidationException("Статус уже был принят как 'APPROVED'");
                case CANCELED:
                    throw new ValidationException("Статус уже был принят как 'CANCELED'");
                default:
                    if (approved) {
                        booking.setStatus(Status.APPROVED);
                    } else {
                        booking.setStatus(Status.REJECTED);
                    }
            }
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
            throw new ValidationException("Item is not Available");
        }
        booking.setItem(item);
    }
    private void validate(Booking booking, Long userId) {
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Бронь в прошедшем времени");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Завершение брони раньше ее регистрации");
        }

        if (userId.equals(booking.getItem().getOwner().getId())) {
            throw new ValidationException("Предмет не может быть забронирован у своего обладателя");
        }
    }

}
