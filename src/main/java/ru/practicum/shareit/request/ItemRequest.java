package ru.practicum.shareit.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {

    private Integer idRequest;

    private Integer idUser;

    private String name;

    private Integer responseItemId;
}
