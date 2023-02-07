package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long userId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long userId,
                                                             LocalDateTime t1, LocalDateTime t2,
                                                             Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBefore(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfter(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(Long userId, Status status, Pageable pageable);

    List<Booking> findAllByItem_OwnerId(Long userId, Pageable pageable);

    List<Booking> findAllByItem_OwnerIdAndStartBeforeAndEndAfter(Long userId,
                                                                 LocalDateTime t1, LocalDateTime t2,
                                                                 Pageable pageable);

    List<Booking> findAllByItem_OwnerIdAndEndBefore(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_OwnerIdAndStartAfter(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_OwnerIdAndStatus(Long userId, Status waiting, Pageable pageable);

    Optional<Booking> findFirstByItemIdAndStartLessThanEqual(Long id, LocalDateTime now, Sort sort);

    Optional<Booking> findFirstByItemIdAndStartAfter(Long id, LocalDateTime now, Sort sort);

    List<Booking> findAllByItemIdAndBookerIdAndEndBefore(Long itemId, Long userId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdInAndStartLessThanEqual(List<Long> idItems, LocalDateTime now, Sort sort);

    Optional<Booking> findFirstByItemIdInAndStartAfter(List<Long> idItems, LocalDateTime now, Sort sort);
}
