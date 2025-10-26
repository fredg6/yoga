package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserDetailsServiceImplTest {
    private User user;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
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
    void givenAnUnexistingUsername_whenCallIsMadeToLoadUserByUsername_thenThrowsUsernameNotFoundException() {
        user.setEmail("unexisting@test.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        var exception = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(user.getEmail()));

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        assertThat(exception.getMessage()).isEqualTo("User Not Found with email: " + user.getEmail());
    }

    @Test
    void givenAnExistingUsername_whenCallIsMadeToLoadUserByUsername_thenReturnsUserDetails() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        var actualUserDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var expectedUserDetails = UserDetailsImpl.builder().id(user.getId()).build();

        assertThat(actualUserDetails).isEqualTo(expectedUserDetails);
    }
}