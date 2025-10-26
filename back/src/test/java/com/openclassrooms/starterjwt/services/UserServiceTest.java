package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    private User user;

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Test")
                .firstName("Test")
                .password("test!1234")
                .admin(false)
                .build();
    }

    @Test
    void givenTheIdOfAUser_whenCallIsMadeToDelete_thenDeletesTheUser() {
        doNothing().when(userRepository).deleteById(user.getId());

        userService.delete(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void givenTheIdOfAnExistingUser_whenCallIsMadeToFindById_thenReturnsTheUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        var foundUser = userService.findById(user.getId());

        verify(userRepository, times(1)).findById(user.getId());
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void givenTheIdOfAnUnexistingUser_whenCallIsMadeToFindById_thenReturnsNull() {
        user.setId(99999L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        var foundUser = userService.findById(user.getId());

        verify(userRepository, times(1)).findById(user.getId());
        assertThat(foundUser).isNull();
    }
}