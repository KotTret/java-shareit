package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    long idUser = 1;

    @BeforeEach
    private void addUsers() {
        idUser = userRepository.save(User.builder()
                .name("Kot")
                .email("kot@tret.ruru")
                .build()).getId();
    }

    @Test
    void deleteById() {
        userRepository.deleteById(idUser);

        assertEquals(List.of(), userRepository.findAll());
    }
}