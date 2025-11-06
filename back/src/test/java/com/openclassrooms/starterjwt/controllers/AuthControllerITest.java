package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerITest {
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .email("yoga@studio.com")
                .lastName("Admin")
                .firstName("Admin")
                .password("test!1234")
                .admin(true)
                .build();
    }

    @Test
    void givenGoodCredentials_whenRequestIsMadeToLogin_thenReturnsResponseWithOKStatusAndJwtResponseJson() throws Exception {
        var loginRequestJson = "{ \"email\": \"yoga@studio.com\", \"password\": \"test!1234\" }";

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.admin").value(user.isAdmin()))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void givenBadCredentials_whenRequestIsMadeToLogin_thenReturnsResponseWithUnauthorizedStatus() throws Exception {
        var loginRequestJson = "{ \"email\": \"unauthorized@test.com\", \"password\": \"test!1234\" }";

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginRequestJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenASignupRequestWithEmailAlreadyTaken_whenRequestIsMadeToRegister_thenReturnsResponseWithBadRequestStatusAndErrorMessageResponseJson() throws Exception {
        var signupRequestJson = "{ \"email\": \"yoga@studio.com\", \"password\": \"test!1234\", \"firstName\": \"Test\", \"lastName\": \"Test\" }";

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(signupRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

    @Test
    void givenAValidSignupRequest_whenRequestIsMadeToRegister_thenUserIsRegisteredAndCanLogin() throws Exception {
        var signupRequestJson = "{ \"email\": \"test2@test.com\", \"password\": \"test!12345\", \"firstName\": \"Test2\", \"lastName\": \"Test2\" }";

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(signupRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        user.setId(3L);
        user.setEmail("test2@test.com");
        user.setFirstName("Test2");
        user.setLastName("Test2");
        user.setPassword("test!12345");
        user.setAdmin(false);
        var loginRequestJson = "{ \"email\": \"test2@test.com\", \"password\": \"test!12345\" }";

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.admin").value(user.isAdmin()))
                .andExpect(jsonPath("$.token").exists());
    }
}