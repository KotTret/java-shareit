package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterId(Long userId, Pageable pageable);

    @Query(" select i from ItemRequest i " +
            "where i.requester.id  <>  ?1 ")
    List<ItemRequest> findAllByUserId(Long userId, Pageable pageable);
}
