package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherTest {
    private Teacher teacher;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void init() {
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Teacher")
                .firstName("Margaret")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void whenCallIsMadeToNoArgsConstructor_thenInstanceIsConstructedAndGettersReturnNullValues() {
        teacher = new Teacher();

        assertThat(teacher.getId()).isNull();
        assertThat(teacher.getLastName()).isNull();
        assertThat(teacher.getFirstName()).isNull();
        assertThat(teacher.getCreatedAt()).isNull();
        assertThat(teacher.getUpdatedAt()).isNull();
    }

    @Test
    void givenAllArgs_whenCallIsMadeToAllArgsConstructor_thenInstanceIsConstructedAndGettersReturnSetData() {
        teacher = new Teacher(1L, "Teacher", "Margaret", now, now);

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Teacher");
        assertThat(teacher.getFirstName()).isEqualTo("Margaret");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void givenAllFieldsValuesAreSet_whenCallIsMadeToGetters_thenReturnsCorrectValues() {
        var now = LocalDateTime.now();

        teacher.setId(2L);
        teacher.setFirstName("Herbert");
        teacher.setLastName("Garrison");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(2L);
        assertThat(teacher.getLastName()).isEqualTo("Garrison");
        assertThat(teacher.getFirstName()).isEqualTo("Herbert");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void givenTwoTeachersWithSameId_whenCallIsMadeToEqualsAndHashCode_thenEqual() {
        var teacher1 = teacher;

        var now = LocalDateTime.now();
        var teacher2 = new Teacher(1L, "Garrison", "Herbert", now, now);

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    }

    @Test
    void givenTwoTeachersWithDifferentId_whenCallIsMadeToEqualsAndHashCode_thenNotEqual() {
        var teacher1 = teacher;

        var now = LocalDateTime.now();
        var teacher2 = new Teacher(2L, "Garrison", "Herbert", now, now);

        assertThat(teacher1).isNotEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher2.hashCode());
    }

    @Test
    void givenATeacher_whenCallIsMadeToToString_thenReturnIsNotNullAndContainsAllFieldsValues() {
        var toString = teacher.toString();

        assertThat(toString).isNotNull();
        assertThat(toString).contains("1");
        assertThat(toString).contains("Teacher");
        assertThat(toString).contains("Margaret");
        assertThat(toString).contains(now.toString());
    }
}