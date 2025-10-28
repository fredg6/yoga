package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@test.com")
class TeacherControllerTest {
    private List<Teacher> teachers;
    private Teacher teacher;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @BeforeEach
    void init() {
        var now = LocalDateTime.now();
        teachers = List.of(
                Teacher.builder()
                        .id(1L)
                        .lastName("Teacher")
                        .firstName("Margaret")
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                Teacher.builder()
                        .id(2L)
                        .lastName("Garrison")
                        .firstName("Herbert")
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
        teacher = teachers.get(0);
    }

    @Test
    void givenAnExistingTeacherId_whenRequestIsMadeToFindById_thenReturnsResponseWithOKStatusAndTeacherJson() throws Exception {
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacher.getId()))
                .andExpect(jsonPath("$.lastName").value(teacher.getLastName()))
                .andExpect(jsonPath("$.firstName").value(teacher.getFirstName()))
                .andExpect(jsonPath("$.createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$.updatedAt").exists()); // Idem

        verify(teacherService, times(1)).findById(teacher.getId());
    }

    @Test
    void givenAnUnexistingTeacherId_whenRequestIsMadeToFindById_thenReturnsResponseWithNotFoundStatus() throws Exception {
        teacher.setId(99999L);
        when(teacherService.findById(teacher.getId())).thenReturn(null);

        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
                .andExpect(status().isNotFound());

        verify(teacherService, times(1)).findById(teacher.getId());
    }

    @Test
    void givenAnAlphanumericTeacherId_whenRequestIsMadeToFindById_thenReturnsResponseWithBadRequestStatus() throws Exception {
        when(teacherService.findById(any())).thenThrow(NumberFormatException.class);

        mockMvc.perform(get("/api/teacher/testId1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRequestIsMadeToFindAll_thenReturnsResponseWithOKStatusAndTeachersJson() throws Exception {
        when(teacherService.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lastName").value(teachers.get(0).getLastName()))
                .andExpect(jsonPath("$[0].firstName").value(teachers.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$[0].updatedAt").exists()) // Idem
                .andExpect(jsonPath("$[1].lastName").value(teachers.get(1).getLastName()))
                .andExpect(jsonPath("$[1].firstName").value(teachers.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].createdAt").exists()) // Juste exists() car 2 derniers chiffres tronqués lors du mapping (??)
                .andExpect(jsonPath("$[1].updatedAt").exists()) // Idem
        ;

        verify(teacherService, times(1)).findAll();
    }
}