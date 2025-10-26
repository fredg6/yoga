package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class TeacherServiceTest {
    private Teacher teacher;

    @Autowired
    private TeacherService teacherService;
    @MockBean
    private TeacherRepository teacherRepository;

    @BeforeEach
    void init() {
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Teacher")
                .firstName("Margaret")
                .build();
    }

    @Test
    void findAll_shouldReturnAllExistingTeachers() {
        var teachers = List.of(teacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        var allTeachers = teacherService.findAll();

        verify(teacherRepository, times(1)).findAll();
        assertThat(allTeachers).hasSize(teachers.size());
    }

    @Test
    void givenTheIdOfAnExistingTeacher_whenCallIsMadeToFindById_thenReturnsTheTeacher() {
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));

        var foundTeacher = teacherService.findById(teacher.getId());

        verify(teacherRepository, times(1)).findById(teacher.getId());
        assertThat(foundTeacher).isEqualTo(teacher);
    }

    @Test
    void givenTheIdOfAnUnexistingTeacher_whenCallIsMadeToFindById_thenReturnsNull() {
        teacher.setId(99999L);
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.empty());

        var foundTeacher = teacherService.findById(teacher.getId());

        verify(teacherRepository, times(1)).findById(teacher.getId());
        assertThat(foundTeacher).isNull();
    }
}