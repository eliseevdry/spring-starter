package org.example.integration.service;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Role;
import org.example.dto.UserCreateEditDto;
import org.example.dto.UserReadDto;
import org.example.integration.BaseIT;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class UserServiceIT extends BaseIT {
    public static final Long USER_1 = 1L;
    public static final Integer COMPANY_1 = 1;

    private final UserService userService;

    @Test
    void findAll() {
        List<UserReadDto> users = userService.findAll();
        assertThat(users).hasSize(5);
    }

    @Test
    void findById() {
        Optional<UserReadDto> optionalUserReadDto = userService.findById(USER_1);
        assertTrue(optionalUserReadDto.isPresent());
        assertEquals(optionalUserReadDto.get().getUsername(), "ivan@gmail.com");
    }

    @Test
    void create() {
        UserCreateEditDto userCreateEditDto =
            new UserCreateEditDto("lexa@mail.ru", LocalDate.now(), "lexa", "voronov", Role.USER, COMPANY_1);
        UserReadDto userReadDto = userService.create(userCreateEditDto);
        assertEquals(userReadDto.getUsername(), "lexa@mail.ru");
    }

    @Test
    void update() {
        UserCreateEditDto userCreateEditDto =
            new UserCreateEditDto("lexa@mail.ru", LocalDate.now(), "lexa", "voronov", Role.USER, 1);

        Optional<UserReadDto> optionalUserReadDto = userService.update(USER_1, userCreateEditDto);
        assertTrue(optionalUserReadDto.isPresent());
        assertEquals(optionalUserReadDto.get().getUsername(), "lexa@mail.ru");
    }

    @Test
    void delete() {
        boolean result = userService.delete(USER_1);
        assertTrue(result);
    }
}
