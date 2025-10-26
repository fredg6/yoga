package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserMapperTest {
    private User user;
    private UserDto userDto;

    @Autowired
    private UserMapper userMapper;

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
        userDto = new UserDto(1L, "test@test.com", "Test", "Test", false, "test!1234", now, now);
    }

    @Test
    void givenAUserDto_whenCallIsMadeToToEntity_thenReturnsAUser() {
        var actualUser = userMapper.toEntity(userDto);
        var expectedUser = user;

        assertThat(actualUser)
                .hasFieldOrPropertyWithValue("id", expectedUser.getId())
                .hasFieldOrPropertyWithValue("email", expectedUser.getEmail())
                .hasFieldOrPropertyWithValue("lastName", expectedUser.getLastName())
                .hasFieldOrPropertyWithValue("firstName", expectedUser.getFirstName())
                .hasFieldOrPropertyWithValue("admin", expectedUser.isAdmin())
                .hasFieldOrPropertyWithValue("password", expectedUser.getPassword())
                .hasFieldOrPropertyWithValue("createdAt", expectedUser.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", expectedUser.getUpdatedAt())
        ;
    }

    @Test
    void givenAUser_whenCallIsMadeToToDto_thenReturnsAUserDto() {
        var actualUserDto = userMapper.toDto(user);
        var expectedUserDto = userDto;

        assertThat(actualUserDto)
                .hasFieldOrPropertyWithValue("id", expectedUserDto.getId())
                .hasFieldOrPropertyWithValue("email", expectedUserDto.getEmail())
                .hasFieldOrPropertyWithValue("lastName", expectedUserDto.getLastName())
                .hasFieldOrPropertyWithValue("firstName", expectedUserDto.getFirstName())
                .hasFieldOrPropertyWithValue("admin", expectedUserDto.isAdmin())
                .hasFieldOrPropertyWithValue("password", expectedUserDto.getPassword())
                .hasFieldOrPropertyWithValue("createdAt", expectedUserDto.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", expectedUserDto.getUpdatedAt())
        ;
    }
}