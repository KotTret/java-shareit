package ru.practicum.shareit.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemRequest {

    private Integer idRequest;

    private Integer userId;

    private String description;

    private Integer responseItemId;

    private LocalDateTime date;
}
