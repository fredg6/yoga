package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {
    private UserDetailsImpl userDetails;

    @BeforeEach
    void init() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("Test")
                .lastName("Testt")
                .admin(false)
                .password("test!1234")
                .build();
    }

    @Test
    void givenAllArgs_whenCallIsMadeToAllArgsConstructor_thenInstanceIsConstructedAndGettersReturnSetData() {
        userDetails = new UserDetailsImpl(1L, "test@test.com", "Test", "Testt", false, "test!1234");

        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("test@test.com");
        assertThat(userDetails.getFirstName()).isEqualTo("Test");
        assertThat(userDetails.getLastName()).isEqualTo("Testt");
        assertThat(userDetails.getAdmin()).isFalse();
        assertThat(userDetails.getPassword()).isEqualTo("test!1234");
    }

    @Test
    void givenTwoUserDetailsWithSameId_whenCallIsMadeToEquals_thenEqual() {
        var userDetails1 = userDetails;

        var userDetails2 = new UserDetailsImpl(1L, "test2@test.com", "Test2", "Testt2", true, "test!12345");

        assertThat(userDetails1).isEqualTo(userDetails2);
    }
}