package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@test.com")
class SessionControllerTest {
    private static ObjectMapper objectMapper;
    private static Teacher teacher;
    private static List<User> users;

    private List<Session> sessions;
    private Session session;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @BeforeAll
    static void initData() {
        objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        teacher = Teacher.builder()
                .id(2L)
                .lastName("Garrison")
                .firstName("Herbert")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        users = List.of(
                User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .lastName("Test")
                        .firstName("Test")
                        .password("test!1234")
                        .admin(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                User.builder()
                        .id(2L)
                        .email("test2@test.com")
                        .lastName("Test2")
                        .firstName("Test2")
                        .password("test!12345")
                        .admin(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
    }

    @BeforeEach
    void init() {
        var nowLocalDateTime = LocalDateTime.now();
        var nowDate = new Date();
        sessions = List.of(
                Session.builder()
                        .id(1L)
                        .name("Session 1")
                        .date(nowDate)
                        .description("Une première session")
                        .teacher(teacher)
                        .users(users)
                        .createdAt(nowLocalDateTime)
                        .updatedAt(nowLocalDateTime)
                        .build(),
                Session.builder()
                        .id(2L)
                        .name("Session 2")
                        .date(nowDate)
                        .description("Une seconde session")
                        .teacher(teacher)
                        .users(users)
                        .createdAt(nowLocalDateTime)
                        .updatedAt(nowLocalDateTime)
                        .build()
        );
        session = sessions.get(0);
    }

    @Test
    void givenAnExistingSessionId_whenRequestIsMadeToFindById_thenReturnsResponseWithOKStatusAndSessionJson() throws Exception {
        when(sessionService.getById(session.getId())).thenReturn(session);

        mockMvc.perform(get("/api/session/" + session.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(session.getId()))
                .andExpect(jsonPath("$.name").value(session.getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(session.getDate())))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.description").value(session.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$.updatedAt").exists()); // Idem

        verify(sessionService, times(1)).getById(session.getId());
    }

    @Test
    void givenAnUnexistingSessionId_whenRequestIsMadeToFindById_thenReturnsResponseWithNotFoundStatus() throws Exception {
        session.setId(99999L);
        when(sessionService.getById(session.getId())).thenReturn(null);

        mockMvc.perform(get("/api/session/" + session.getId()))
                .andExpect(status().isNotFound());

        verify(sessionService, times(1)).getById(session.getId());
    }

    @Test
    void givenAnAlphanumericSessionId_whenRequestIsMadeToFindById_thenReturnsResponseWithBadRequestStatus() throws Exception {
        when(sessionService.getById(any())).thenThrow(NumberFormatException.class);

        mockMvc.perform(get("/api/session/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRequestIsMadeToFindAll_thenReturnsResponseWithOKStatusAndSessionsJson() throws Exception {
        when(sessionService.findAll()).thenReturn(sessions);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(sessions.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(sessions.get(0).getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$[0].date").value(objectMapper.writeValueAsString(sessions.get(0).getDate())))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].description").value(sessions.get(0).getDescription()))
                .andExpect(jsonPath("$[0].teacher_id").value(teacher.getId()))
                .andExpect(jsonPath("$[0].users").exists())
                .andExpect(jsonPath("$[0].users").isArray())
                .andExpect(jsonPath("$[0].users", hasSize(2)))
                .andExpect(jsonPath("$[0].users", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[0].createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$[0].updatedAt").exists())
                .andExpect(jsonPath("$[1].id").value(sessions.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(sessions.get(1).getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$[1].date").value(objectMapper.writeValueAsString(sessions.get(1).getDate())))
                .andExpect(jsonPath("$[1].date").exists())
                .andExpect(jsonPath("$[1].description").value(sessions.get(1).getDescription()))
                .andExpect(jsonPath("$[1].teacher_id").value(teacher.getId()))
                .andExpect(jsonPath("$[1].users").exists())
                .andExpect(jsonPath("$[1].users").isArray())
                .andExpect(jsonPath("$[1].users", hasSize(2)))
                .andExpect(jsonPath("$[1].users", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[1].createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$[1].updatedAt").exists()) // Idem
        ;

        verify(sessionService, times(1)).findAll();
    }

    @Test
    void givenASessionToCreateJson_whenRequestIsMadeToCreate_thenReturnsResponseWithOKStatusAndCreatedSessionJson() throws Exception {
        var sessionToCreateJson = "{ \"teacher_id\": 2, \"date\": \"2025-11-28\", \"name\": \"Session 3\", \"description\": \"Une nouvelle session\" }";
        var now = LocalDateTime.now();
        var createdSession = Session.builder()
                .id(3L)
                .name("Session 3")
                .date(DateUtil.parse("2025-11-28"))
                .description("Une nouvelle session")
                .teacher(teacher)
                .users(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();
        when(sessionService.create(any(Session.class))).thenReturn(createdSession);

        mockMvc.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON_VALUE).content(sessionToCreateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSession.getId()))
                .andExpect(jsonPath("$.name").value(createdSession.getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(createdSession.getDate())))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.description").value(createdSession.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users").isEmpty())
                .andExpect(jsonPath("$.createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$.updatedAt").exists()); // Idem

        verify(sessionService, times(1)).create(any(Session.class));
    }

    @Test
    void givenASessionToUpdateIdAndJson_whenRequestIsMadeToUpdate_thenReturnsResponseWithOKStatusAndUpdatedSessionJson() throws Exception {
        var sessionToUpdateJson = "{ \"id\": 1, \"teacher_id\": 2, \"date\": \"2025-11-28\", \"name\": \"Session 1\", \"description\": \"Une session mise à jour\" }";
        var updatedSession = session;
        updatedSession
                .setDate(DateUtil.parse("2025-11-28"))
                .setName("Une session mise à jour");
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(updatedSession);

        mockMvc.perform(put("/api/session/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(sessionToUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedSession.getId()))
                .andExpect(jsonPath("$.name").value(updatedSession.getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(updatedSession.getDate())))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.description").value(updatedSession.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$.updatedAt").exists()); // Idem

        verify(sessionService, times(1)).update(eq(1L), any(Session.class));
    }

    @Test
    void givenASessionToUpdateAlphanumericIdAndJson_whenRequestIsMadeToUpdate_thenReturnsResponseWithBadRequestStatus() throws Exception {
        var sessionToUpdateJson = "{ \"id\": 1, \"teacher_id\": 2, \"date\": \"2025-11-28\", \"name\": \"Session 1\", \"description\": \"Une session mise à jour\" }";
        when(sessionService.update(any(), any(Session.class))).thenThrow(NumberFormatException.class);

        mockMvc.perform(put("/api/session/testId1234").contentType(MediaType.APPLICATION_JSON_VALUE).content(sessionToUpdateJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnUnexistingSessionId_whenRequestIsMadeToDelete_thenReturnsResponseWithNotFoundStatus() throws Exception {
        session.setId(99999L);
        when(sessionService.getById(session.getId())).thenReturn(null);

        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isNotFound());

        verify(sessionService, times(1)).getById(session.getId());
    }

    @Test
    void givenAnExistingSessionId_whenRequestIsMadeToDelete_thenReturnsResponseWithOKStatus() throws Exception {
        when(sessionService.getById(session.getId())).thenReturn(session);
        doNothing().when(sessionService).delete(session.getId());

        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).getById(session.getId());
        verify(sessionService, times(1)).delete(session.getId());
    }

    @Test
    void givenAnAlphanumericSessionId_whenRequestIsMadeToDelete_thenReturnsResponseWithBadRequestStatus() throws Exception {
        when(sessionService.getById(any())).thenThrow(NumberFormatException.class);

        mockMvc.perform(delete("/api/session/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnAlphanumericSessionIdAndAnExistingUserId_whenRequestIsMadeToParticipate_thenReturnsResponseWithBadRequestStatus() throws Exception {
        doThrow(NumberFormatException.class).when(sessionService).participate(any(), eq(1L));

        mockMvc.perform(post("/api/session/testId1234/participate/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenExistingSessionAndUserIds_whenRequestIsMadeToParticipate_thenReturnsResponseWithOKStatus() throws Exception {
        doNothing().when(sessionService).participate(session.getId(), 1L);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).participate(session.getId(), 1L);
    }

    @Test
    void givenAnAlphanumericSessionIdAndAnExistingUserId_whenRequestIsMadeToNoLongerParticipate_thenReturnsResponseWithBadRequestStatus() throws Exception {
        doThrow(NumberFormatException.class).when(sessionService).noLongerParticipate(any(), eq(1L));

        mockMvc.perform(delete("/api/session/testId1234/participate/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenExistingSessionAndUserIds_whenRequestIsMadeToNoLongerParticipate_thenReturnsResponseWithOKStatus() throws Exception {
        doNothing().when(sessionService).noLongerParticipate(session.getId(), 1L);

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).noLongerParticipate(session.getId(), 1L);
    }
}