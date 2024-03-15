package org.example.integration.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.integration.BaseIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.dto.UserCreateEditDto.Fields.birthDate;
import static org.example.dto.UserCreateEditDto.Fields.companyId;
import static org.example.dto.UserCreateEditDto.Fields.firstname;
import static org.example.dto.UserCreateEditDto.Fields.image;
import static org.example.dto.UserCreateEditDto.Fields.lastname;
import static org.example.dto.UserCreateEditDto.Fields.password;
import static org.example.dto.UserCreateEditDto.Fields.role;
import static org.example.dto.UserCreateEditDto.Fields.username;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends BaseIT {

    private final MockMvc mockMvc;

    @BeforeEach
    void startup() {
//        List<GrantedAuthority> roles = Arrays.asList(Role.ADMIN, Role.USER);
//        User testUser = new User("test@gmail.com", "test", roles);
//        TestingAuthenticationToken token = new TestingAuthenticationToken(testUser, testUser.getPassword(), roles);
//
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(token);
//
//        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("filter"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param(username, "test@mail.ru")
                        .param(password, "test")
                        .param(birthDate, "2000-01-01")
                        .param(firstname, "test")
                        .param(lastname, "test")
                        .param(role, "USER")
                        .param(companyId, "1")
                        .param(image, "")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );

    }
}