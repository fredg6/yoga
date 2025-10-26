package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeacherMapperTest {
    private Teacher teacher;
    private TeacherDto teacherDto;

    @Autowired
    private TeacherMapper teacherMapper;

    @BeforeEach
    void init() {
        var now = LocalDateTime.now();
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Teacher")
                .firstName("Margaret")
                .createdAt(now)
                .updatedAt(now)
                .build();
        teacherDto = new TeacherDto(1L, "Teacher", "Margaret", now, now);
    }

    @Test
    void givenATeacherDto_whenCallIsMadeToToEntity_thenReturnsATeacher() {
        var actualTeacher = teacherMapper.toEntity(teacherDto);
        var expectedTeacher = teacher;

        assertThat(actualTeacher)
                .hasFieldOrPropertyWithValue("id", expectedTeacher.getId())
                .hasFieldOrPropertyWithValue("lastName", expectedTeacher.getLastName())
                .hasFieldOrPropertyWithValue("firstName", expectedTeacher.getFirstName())
                .hasFieldOrPropertyWithValue("createdAt", expectedTeacher.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", expectedTeacher.getUpdatedAt())
        ;
    }

    @Test
    void givenATeacher_whenCallIsMadeToToDto_thenReturnsATeacherDto() {
        var actualTeacherDto = teacherMapper.toDto(teacher);
        var expectedTeacherDto = teacherDto;

        assertThat(actualTeacherDto)
                .hasFieldOrPropertyWithValue("id", expectedTeacherDto.getId())
                .hasFieldOrPropertyWithValue("lastName", expectedTeacherDto.getLastName())
                .hasFieldOrPropertyWithValue("firstName", expectedTeacherDto.getFirstName())
                .hasFieldOrPropertyWithValue("createdAt", expectedTeacherDto.getCreatedAt())
                .hasFieldOrPropertyWithValue("updatedAt", expectedTeacherDto.getUpdatedAt())
        ;
    }
}