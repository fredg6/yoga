package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class SessionMapperTest {
    private Session session;
    private Teacher teacher;
    private User user;
    private SessionDto sessionDto;

    @Autowired
    private SessionMapper sessionMapper;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private UserService userService;

    @BeforeEach
    void init() {
        var nowDate = new Date();
        var nowLocalDateTime = LocalDateTime.now();
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Teacher")
                .firstName("Margaret")
                .build();
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
                .date(nowDate)
                .description("Une session")
                .teacher(teacher)
                .users(List.of(user))
                .createdAt(nowLocalDateTime)
                .updatedAt(nowLocalDateTime)
                .build();
        sessionDto = new SessionDto(1L, "Session", nowDate, 1L, "Une session", List.of(1L), nowLocalDateTime, nowLocalDateTime);
    }

    @Test
    void givenASessionDto_whenCallIsMadeToToEntity_thenReturnsASession() {
        when(teacherService.findById(sessionDto.getTeacher_id())).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        var actualSession = sessionMapper.toEntity(sessionDto);
        var expectedSession = session;

        assertThat(actualSession)
                .hasFieldOrPropertyWithValue("id", expectedSession.getId())
                .hasFieldOrPropertyWithValue("name", expectedSession.getName())
                .hasFieldOrPropertyWithValue("date", expectedSession.getDate())
                .hasFieldOrPropertyWithValue("description", expectedSession.getDescription())
                .hasFieldOrPropertyWithValue("teacher", expectedSession.getTeacher())
                .hasFieldOrPropertyWithValue("users", expectedSession.getUsers())
                .hasFieldOrPropertyWithValue("createdAt", expectedSession.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", expectedSession.getUpdatedAt())
        ;
    }

    @Test
    void givenASession_whenCallIsMadeToToDto_thenReturnsASessionDto() {
        var actualSessionDto = sessionMapper.toDto(session);
        var expectedSessionDto = sessionDto;

        assertThat(actualSessionDto)
                .hasFieldOrPropertyWithValue("id", expectedSessionDto.getId())
                .hasFieldOrPropertyWithValue("name", expectedSessionDto.getName())
                .hasFieldOrPropertyWithValue("date", expectedSessionDto.getDate())
                .hasFieldOrPropertyWithValue("description", expectedSessionDto.getDescription())
                .hasFieldOrPropertyWithValue("teacher_id", expectedSessionDto.getTeacher_id())
                .hasFieldOrPropertyWithValue("users", expectedSessionDto.getUsers())
                .hasFieldOrPropertyWithValue("createdAt", expectedSessionDto.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", expectedSessionDto.getUpdatedAt())
        ;
    }
}