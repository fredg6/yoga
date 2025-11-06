package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@test.com")
class TeacherControllerITest {
    private List<Teacher> teachers;
    private Teacher teacher;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        teachers = List.of(
                Teacher.builder()
                        .id(1L)
                        .lastName("DELAHAYE")
                        .firstName("Margot")
                        .build(),
                Teacher.builder()
                        .id(2L)
                        .lastName("THIERCELIN")
                        .firstName("Helene")
                        .build()
        );
        teacher = teachers.get(0);
    }

    @Test
    void givenAnExistingTeacherId_whenRequestIsMadeToFindById_thenReturnsResponseWithOKStatusAndTeacherJson() throws Exception {
        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacher.getId()))
                .andExpect(jsonPath("$.lastName").value(teacher.getLastName()))
                .andExpect(jsonPath("$.firstName").value(teacher.getFirstName()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void givenAnUnexistingTeacherId_whenRequestIsMadeToFindById_thenReturnsResponseWithNotFoundStatus() throws Exception {
        mockMvc.perform(get("/api/teacher/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnAlphanumericTeacherId_whenRequestIsMadeToFindById_thenReturnsResponseWithBadRequestStatus() throws Exception {
        mockMvc.perform(get("/api/teacher/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRequestIsMadeToFindAll_thenReturnsResponseWithOKStatusAndTeachersJson() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(teachers.get(0).getId()))
                .andExpect(jsonPath("$[0].lastName").value(teachers.get(0).getLastName()))
                .andExpect(jsonPath("$[0].firstName").value(teachers.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].updatedAt").exists())
                .andExpect(jsonPath("$[1].id").value(teachers.get(1).getId()))
                .andExpect(jsonPath("$[1].lastName").value(teachers.get(1).getLastName()))
                .andExpect(jsonPath("$[1].firstName").value(teachers.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].createdAt").exists())
                .andExpect(jsonPath("$[1].updatedAt").exists())
        ;
    }
}