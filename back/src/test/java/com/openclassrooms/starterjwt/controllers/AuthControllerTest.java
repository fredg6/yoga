package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        var now = LocalDateTime.now();
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Test")
                .firstName("Test")
                .password("test!1234")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void givenGoodCredentials_whenRequestIsMadeToLogin_thenReturnsResponseWithOKStatusAndJwtResponseJson() throws Exception {
        var loginRequestJson = "{ \"email\": \"test@test.com\", \"password\": \"test!1234\" }";
        var userDetails = UserDetailsImpl.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .admin(user.isAdmin())
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword());
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()))).thenReturn(authentication);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.admin").value(user.isAdmin()))
                .andExpect(jsonPath("$.token").exists());

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void givenASignupRequestWithEmailAlreadyTaken_whenRequestIsMadeToRegister_thenReturnsResponseWithBadRequestStatusAndErrorMessageResponseJson() throws Exception {
        var signupRequestJson = "{ \"email\": \"already.taken@test.com\", \"password\": \"test!1234\", \"firstName\": \"Test\", \"lastName\": \"Test\" }";
        when(userRepository.existsByEmail("already.taken@test.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(signupRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

        verify(userRepository, times(1)).existsByEmail("already.taken@test.com");
    }

    @Test
    void givenAValidSignupRequest_whenRequestIsMadeToRegister_thenReturnsResponseWithOKStatusAndSuccessMessageResponseJson() throws Exception {
        var signupRequestJson = "{ \"email\": \"test@test.com\", \"password\": \"test!1234\", \"firstName\": \"Test\", \"lastName\": \"Test\" }";
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(signupRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }
}