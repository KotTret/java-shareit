package ru.practicum.shareit.review;

import lombok.*;
import ru.practicum.shareit.valodation.Create;
import ru.practicum.shareit.valodation.Update;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @NotNull(groups = Update.class)
    private Integer reviewId;
    private String content;
    @NotNull(groups = {Create.class, Update.class})
    private Boolean isPositive;
    @NotNull(groups = Create.class)
    private Integer userId;
    @NotNull(groups = Create.class)
    private Integer itemId;

}
