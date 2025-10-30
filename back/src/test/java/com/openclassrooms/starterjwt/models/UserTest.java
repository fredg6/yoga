package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private User user;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Testt")
                .firstName("Test")
                .password("test!1234")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void whenCallIsMadeToNoArgsConstructor_thenInstanceIsConstructedAndGettersReturnNullValues() {
        user = new User();

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getLastName()).isNull();
        assertThat(user.getFirstName()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    void givenAllArgs_whenCallIsMadeToAllArgsConstructor_thenInstanceIsConstructedAndGettersReturnSetData() {
        user = new User(1L, "test@test.com", "Testt", "Test", "test!1234", false, now, now);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getLastName()).isEqualTo("Testt");
        assertThat(user.getFirstName()).isEqualTo("Test");
        assertThat(user.getPassword()).isEqualTo("test!1234");
        assertThat(user.isAdmin()).isFalse();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void givenRequiredArgs_whenCallIsMadeToRequiredArgsConstructor_thenInstanceIsConstructedAndGettersReturnSetData() {
        user = new User("test@test.com", "Testt", "Test", "test!1234", false);

        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getLastName()).isEqualTo("Testt");
        assertThat(user.getFirstName()).isEqualTo("Test");
        assertThat(user.getPassword()).isEqualTo("test!1234");
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    void givenAllFieldsValuesAreSet_whenCallIsMadeToGetters_thenReturnsCorrectValues() {
        var now = LocalDateTime.now();

        user.setId(2L);
        user.setEmail("test2@test.com");
        user.setFirstName("Test2");
        user.setLastName("Testt2");
        user.setPassword("test!12345");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getEmail()).isEqualTo("test2@test.com");
        assertThat(user.getLastName()).isEqualTo("Testt2");
        assertThat(user.getFirstName()).isEqualTo("Test2");
        assertThat(user.getPassword()).isEqualTo("test!12345");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void givenTwoUsersWithSameId_whenCallIsMadeToEqualsAndHashCode_thenEqual() {
        var user1 = user;

        var now = LocalDateTime.now();
        var user2 = new User(1L, "test2@test.com", "Testt2", "Test2", "test!12345", true, now, now);

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void givenTwoUsersWithDifferentId_whenCallIsMadeToEqualsAndHashCode_thenNotEqual() {
        var user1 = user;

        var now = LocalDateTime.now();
        var user2 = new User(2L, "test2@test.com", "Testt2", "Test2", "test!12345", true, now, now);

        assertThat(user1).isNotEqualTo(user2);
        assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());
    }

    @Test
    void givenAUser_whenCallIsMadeToToString_thenReturnIsNotNullAndContainsAllFieldsValues() {
        var toString = user.toString();

        assertThat(toString).isNotNull();
        assertThat(toString).contains("1");
        assertThat(toString).contains("test@test.com");
        assertThat(toString).contains("Testt");
        assertThat(toString).contains("Test");
        assertThat(toString).contains("test!1234");
        assertThat(toString).contains("false");
        assertThat(toString).contains(now.toString());
    }
}