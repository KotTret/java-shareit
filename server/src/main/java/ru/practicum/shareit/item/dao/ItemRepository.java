package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(User owner, Pageable pageable);

    Optional<Item> findByIdAndOwner(Long itemId, User owner);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true ")
    List<Item> search(String text, Pageable pageable);

    List<Item> findByItemRequestIdIn(List<Long> idRequests, Sort id);

    List<Item> findByItemRequestId(Long id, Sort created);
}
