package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.dao.BookingRepository;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findAllByBookerId() {
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter() {
    }

    @Test
    void findAllByBookerIdAndEndBefore() {
    }

    @Test
    void findAllByBookerIdAndStartAfter() {
    }

    @Test
    void findAllByBookerIdAndStatus() {
    }

    @Test
    void findAllByItem_OwnerId() {
    }

    @Test
    void findAllByItem_OwnerIdAndStartBeforeAndEndAfter() {
    }

    @Test
    void findAllByItem_OwnerIdAndEndBefore() {
    }

    @Test
    void findAllByItem_OwnerIdAndStartAfter() {
    }

    @Test
    void findAllByItem_OwnerIdAndStatus() {
    }

    @Test
    void findFirstByItemIdAndStartLessThanEqual() {
    }

    @Test
    void findFirstByItemIdAndStartAfter() {
    }

    @Test
    void findAllByItemIdAndBookerIdAndEndBefore() {
    }

    @Test
    void findFirstByItemIdInAndStartLessThanEqual() {
    }

    @Test
    void findFirstByItemIdInAndStartAfter() {
    }
}