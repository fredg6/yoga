package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@test.com")
class UserControllerTest {
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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
    void givenAnExistingUserId_whenRequestIsMadeToFindById_thenReturnsResponseWithOKStatusAndUserJson() throws Exception {
        when(userService.findById(user.getId())).thenReturn(user);

        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.admin").value(user.isAdmin()))
                .andExpect(jsonPath("$.createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$.updatedAt").exists()) // Idem
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(userService, times(1)).findById(user.getId());
    }

    @Test
    void givenAnUnexistingUserId_whenRequestIsMadeToFindById_thenReturnsResponseWithNotFoundStatus() throws Exception {
        user.setId(99999L);
        when(userService.findById(user.getId())).thenReturn(null);

        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(user.getId());
    }

    @Test
    void givenAnAlphanumericUserId_whenRequestIsMadeToFindById_thenReturnsResponseWithBadRequestStatus() throws Exception {
        when(userService.findById(any())).thenThrow(NumberFormatException.class);

        mockMvc.perform(get("/api/user/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnUnexistingUserId_whenRequestIsMadeToDelete_thenReturnsResponseWithNotFoundStatus() throws Exception {
        user.setId(99999L);
        when(userService.findById(user.getId())).thenReturn(null);

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(user.getId());
    }

    @Test
    void givenAnExistingUserId_whenRequestIsMadeToDeleteByAnotherUser_thenReturnsResponseWithUnauthorizedStatus() throws Exception {
        user.setEmail("unauthorized@test.com");
        when(userService.findById(user.getId())).thenReturn(user);

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).findById(user.getId());
    }

    @Test
    void givenAnExistingUserId_whenRequestIsMadeToDelete_thenReturnsResponseWithOKStatus() throws Exception {
        when(userService.findById(user.getId())).thenReturn(user);
        doNothing().when(userService).delete(user.getId());

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());

        verify(userService, times(1)).findById(user.getId());
        verify(userService, times(1)).delete(user.getId());
    }

    @Test
    void givenAnAlphanumericUserId_whenRequestIsMadeToDelete_thenReturnsResponseWithBadRequestStatus() throws Exception {
        when(userService.findById(any())).thenThrow(NumberFormatException.class);

        mockMvc.perform(delete("/api/user/testId1234"))
                .andExpect(status().isBadRequest());
    }
}