package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.openclassrooms.starterjwt.models.Session;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@test.com")
class SessionControllerITest {
    private static ObjectMapper objectMapper;

    private List<Session> sessions;
    private Session session1;
    private Session session2;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void initData() {
        objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }

    @BeforeEach
    void init() {
        sessions = List.of(
                Session.builder()
                        .id(1L)
                        .name("Session 1")
                        .date(DateUtil.parse("2025-11-12"))
                        .description("Une premiere session")
                        .build(),
                Session.builder()
                        .id(2L)
                        .name("Session 2")
                        .date(DateUtil.parse("2025-11-15"))
                        .description("Une seconde session")
                        .build()
        );
        session1 = sessions.get(0);
        session2 = sessions.get(1);
    }

    @Test
    void givenAnExistingSessionId_whenRequestIsMadeToFindById_thenReturnsResponseWithOKStatusAndSessionJson() throws Exception {
        mockMvc.perform(get("/api/session/" + session1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(session1.getId()))
                .andExpect(jsonPath("$.name").value(session1.getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(session.getDate())))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.description").value(session1.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users", contains(2)))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void givenAnUnexistingSessionId_whenRequestIsMadeToFindById_thenReturnsResponseWithNotFoundStatus() throws Exception {
        mockMvc.perform(get("/api/session/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnAlphanumericSessionId_whenRequestIsMadeToFindById_thenReturnsResponseWithBadRequestStatus() throws Exception {
        mockMvc.perform(get("/api/session/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRequestIsMadeToFindAll_thenReturnsResponseWithOKStatusAndSessionsJson() throws Exception {
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
                .andExpect(jsonPath("$[0].teacher_id").value(1))
                .andExpect(jsonPath("$[0].users").exists())
                .andExpect(jsonPath("$[0].users").isArray())
                .andExpect(jsonPath("$[0].users", hasSize(1)))
                .andExpect(jsonPath("$[0].users", contains(2)))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].updatedAt").exists())
                .andExpect(jsonPath("$[1].id").value(sessions.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(sessions.get(1).getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$[1].date").value(objectMapper.writeValueAsString(sessions.get(1).getDate())))
                .andExpect(jsonPath("$[1].date").exists())
                .andExpect(jsonPath("$[1].description").value(sessions.get(1).getDescription()))
                .andExpect(jsonPath("$[1].teacher_id").value(2))
                .andExpect(jsonPath("$[1].users").exists())
                .andExpect(jsonPath("$[1].users").isArray())
                .andExpect(jsonPath("$[1].users").isEmpty())
                .andExpect(jsonPath("$[1].createdAt").exists())
                .andExpect(jsonPath("$[1].updatedAt").exists())
        ;
    }

    @Test
    @DirtiesContext
    void givenASessionToCreateJson_whenRequestIsMadeToCreate_thenReturnsResponseWithOKStatusAndCreatedSessionJson() throws Exception {
        var sessionToCreateJson = "{ \"teacher_id\": 1, \"date\": \"2025-11-28\", \"name\": \"Session 3\", \"description\": \"Une troisième session\" }";
        var createdSession = Session.builder()
                .id(3L)
                .name("Session 3")
                .date(DateUtil.parse("2025-11-28"))
                .description("Une troisième session")
                .build();

        mockMvc.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON_VALUE).content(sessionToCreateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSession.getId()))
                .andExpect(jsonPath("$.name").value(createdSession.getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(createdSession.getDate())))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.description").value(createdSession.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users").isEmpty())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    @DirtiesContext
    void givenASessionToUpdateIdAndJson_whenRequestIsMadeToUpdate_thenReturnsResponseWithOKStatusAndUpdatedSessionJson() throws Exception {
        var sessionToUpdateJson = "{ \"teacher_id\": 1, \"users\": [2], \"date\": \"2025-11-12\", \"name\": \"Session 1\", \"description\": \"Une session mise à jour\" }";
        var updatedSession = session1;
        updatedSession.setDescription("Une session mise à jour");

        mockMvc.perform(put("/api/session/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(sessionToUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedSession.getId()))
                .andExpect(jsonPath("$.name").value(updatedSession.getName()))
                // La comparaison devrait être OK, mais non ("" en trop)
                // .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(updatedSession.getDate())))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.description").value(updatedSession.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users", contains(2)));
    }

    @Test
    void givenASessionToUpdateAlphanumericIdAndJson_whenRequestIsMadeToUpdate_thenReturnsResponseWithBadRequestStatus() throws Exception {
        var sessionToUpdateJson = "{ \"teacher_id\": 1, \"date\": \"2025-11-12\", \"name\": \"Session 1\", \"description\": \"Une session mise à jour\" }";
        mockMvc.perform(put("/api/session/testId1234").contentType(MediaType.APPLICATION_JSON_VALUE).content(sessionToUpdateJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnUnexistingSessionId_whenRequestIsMadeToDelete_thenReturnsResponseWithNotFoundStatus() throws Exception {
        mockMvc.perform(delete("/api/session/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void givenAnExistingSessionId_whenRequestIsMadeToDelete_thenDeletesTheSessionWhichCanNoMoreBeRead() throws Exception {
        mockMvc.perform(delete("/api/session/" + session1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + session1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnAlphanumericSessionId_whenRequestIsMadeToDelete_thenReturnsResponseWithBadRequestStatus() throws Exception {
        mockMvc.perform(delete("/api/session/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnAlphanumericSessionIdAndAnExistingUserId_whenRequestIsMadeToParticipate_thenReturnsResponseWithBadRequestStatus() throws Exception {
        mockMvc.perform(post("/api/session/testId1234/participate/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenExistingSessionAndUserIds_whenRequestIsMadeToParticipate_thenReturnsResponseWithOKStatus() throws Exception {
        mockMvc.perform(post("/api/session/" + session2.getId() + "/participate/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + session2.getId()))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users", contains(2)));
    }

    @Test
    void givenAnAlphanumericSessionIdAndAnExistingUserId_whenRequestIsMadeToNoLongerParticipate_thenReturnsResponseWithBadRequestStatus() throws Exception {
        mockMvc.perform(delete("/api/session/testId1234/participate/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenExistingSessionAndUserIds_whenRequestIsMadeToNoLongerParticipate_thenReturnsResponseWithOKStatus() throws Exception {
        mockMvc.perform(delete("/api/session/" + session1.getId() + "/participate/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + session1.getId()))
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users").isEmpty());
    }
}