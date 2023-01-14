package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.review.Review;
import ru.practicum.shareit.valodation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
    @NotNull(groups = Update.class)
    private Integer userId;

    @NotNull
    @Email(message = "Формат электронной почты указан неверно.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы.")
    private String login;

    private String name;

    private List<Item> items = new ArrayList<>();

    private List<Review> reviews = new ArrayList<>();
}
