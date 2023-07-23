package org.example.integration.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserCreateEditDto;
import org.example.dto.UserCreateEditDto.Fields;
import org.example.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.dto.UserCreateEditDto.Fields.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends BaseIT {

    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(
                get("/users")
            )
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("user/users"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("users", hasSize(5)));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(
            post("/users")
                .param(username, "test@mail.ru")
                .param(firstname, "test")
                .param(lastname, "test")
                .param(role, "USER")
                .param(companyId, "1")
                .param(birthDate, "2000-01-01")
        )
            .andExpectAll(
                status().is3xxRedirection(),
                redirectedUrlPattern("/users/{\\d+}")
            );

    }
}