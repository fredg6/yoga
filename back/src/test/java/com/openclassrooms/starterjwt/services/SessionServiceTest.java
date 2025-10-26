package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionServiceTest {
    private Session session;
    private User user;

    @Autowired
    private SessionService sessionService;
    @MockBean
    private SessionRepository sessionRepository;
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
        session = Session.builder()
                .id(1L)
                .name("Session")
                .date(new Date())
                .description("Une session")
                .build();
    }

    @Test
    void givenASessionToCreate_whenCallIsMadeToCreate_thenReturnsTheCreatedSession() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        verify(sessionRepository, times(1)).save(session);
        assertThat(createdSession).isEqualTo(session);
    }

    @Test
    void givenTheIdOfASession_whenCallIsMadeToDelete_thenDeletesTheSession() {
        doNothing().when(sessionRepository).deleteById(session.getId());

        sessionService.delete(session.getId());

        verify(sessionRepository, times(1)).deleteById(session.getId());
    }

    @Test
    void findAll_shouldReturnAllExistingSessions() {
        List<Session> sessions = List.of(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> allSessions = sessionService.findAll();

        verify(sessionRepository, times(1)).findAll();
        assertThat(allSessions).hasSize(sessions.size());
    }

    @Test
    void givenTheIdOfAnExistingSession_whenCallIsMadeToGetById_thenReturnsTheSession() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(session.getId());

        verify(sessionRepository, times(1)).findById(session.getId());
        assertThat(foundSession).isEqualTo(session);
    }

    @Test
    void givenTheIdOfAnUnexistingSession_whenCallIsMadeToGetById_thenReturnsNull() {
        session.setId(99999L);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        Session foundSession = sessionService.getById(session.getId());

        verify(sessionRepository, times(1)).findById(session.getId());
        assertThat(foundSession).isNull();
    }

    @Test
    void givenASessionAndItsId_whenCallIsMadeToUpdate_thenReturnsTheUpdatedSession() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session updatedSession = sessionService.update(session.getId(), session);

        verify(sessionRepository, times(1)).save(session);
        assertThat(updatedSession).isEqualTo(session);
        assertThat(updatedSession.getId()).isEqualTo(session.getId());
    }

    @Test
    void givenTheIdsOfAnUnexistingSessionAndAnExistingUser_whenCallIsMadeToParticipate_thenThrowsNotFoundException() {
        session.setId(99999L);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(NotFoundException.class, () -> sessionService.participate(session.getId(), user.getId()));

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void givenTheIdsOfASessionAndAUserAlreadyParticipatingInThisSession_whenCallIsMadeToParticipate_thenThrowsBadRequestException() {
        session.setUsers(List.of(user));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), user.getId()));
    }

    @Test
    void givenTheIdsOfASessionAndAUserNotYetParticipatingInThisSession_whenCallIsMadeToParticipate_thenAddsTheUserToParticipantsAndSaveTheSession() {
        session.setUsers(new ArrayList<>()); // new ArrayList<>() et non pas List.of() qui crée une List non modifiable
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.participate(session.getId(), user.getId());

        assertThat(session.getUsers()).hasSize(1).contains(user);
    }

    @Test
    void givenTheIdsOfAnUnexistingSessionAndAnExistingUser_whenCallIsMadeToNoLongerParticipate_thenThrowsNotFoundException() {
        session.setId(99999L);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));

        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    void givenTheIdsOfASessionAndAUserNotParticipatingInThisSession_whenCallIsMadeToNoLongerParticipate_thenThrowsBadRequestException() {
        session.setUsers(List.of());
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));
    }

    @Test
    void givenTheIdsOfASessionAndAUserParticipatingInThisSession_whenCallIsMadeToNoLongerParticipate_thenRemovesTheUserFromParticipantsAndSaveTheSession() {
        session.setUsers(List.of(user));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.noLongerParticipate(session.getId(), user.getId());

        assertThat(session.getUsers()).isEmpty();
    }
}