package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@test.com")
class UserControllerITest {
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(2L)
                .email("test@test.com")
                .lastName("Test")
                .firstName("Test")
                .password("test!1234")
                .admin(false)
                .build();
    }

    @Test
    void givenAnExistingUserId_whenRequestIsMadeToFindById_thenReturnsResponseWithOKStatusAndUserJson() throws Exception {
        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.admin").value(user.isAdmin()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void givenAnUnexistingUserId_whenRequestIsMadeToFindById_thenReturnsResponseWithNotFoundStatus() throws Exception {
        mockMvc.perform(get("/api/user/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnAlphanumericUserId_whenRequestIsMadeToFindById_thenReturnsResponseWithBadRequestStatus() throws Exception {
        mockMvc.perform(get("/api/user/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnUnexistingUserId_whenRequestIsMadeToDelete_thenReturnsResponseWithNotFoundStatus() throws Exception {
        mockMvc.perform(delete("/api/user/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void givenAnExistingUserId_whenRequestIsMadeToDeleteByAnotherUser_thenReturnsResponseWithUnauthorizedStatus() throws Exception {
        var unauthorizedUserDetails = UserDetailsImpl.builder().username("unauthorized@test.com").build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(unauthorizedUserDetails, null, unauthorizedUserDetails.getAuthorities())
        );

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void givenAnExistingUserId_whenRequestIsMadeToDelete_thenDeletesTheUserWhichCanNoMoreBeRead() throws Exception {
        // Doit d'abord ne plus participer à la session 1
        mockMvc.perform(delete("/api/session/1/participate/" + user.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnAlphanumericUserId_whenRequestIsMadeToDelete_thenReturnsResponseWithBadRequestStatus() throws Exception {
        mockMvc.perform(delete("/api/user/testId1234"))
                .andExpect(status().isBadRequest());
    }
}