package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTest {
    private Teacher teacher;
    private Session session;
    private final LocalDateTime nowLocalDateTime = LocalDateTime.now();
    private final Date nowDate = new Date();

    @BeforeEach
    void init() {
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Teacher")
                .firstName("Margaret")
                .createdAt(nowLocalDateTime)
                .updatedAt(nowLocalDateTime)
                .build();
        session = Session.builder()
                .id(1L)
                .name("Session")
                .date(nowDate)
                .description("Une session")
                .teacher(teacher)
                .users(List.of())
                .createdAt(nowLocalDateTime)
                .updatedAt(nowLocalDateTime)
                .build();
    }

    @Test
    void whenCallIsMadeToNoArgsConstructor_thenInstanceIsConstructedAndGettersReturnNullValues() {
        session = new Session();

        assertThat(session.getId()).isNull();
        assertThat(session.getName()).isNull();
        assertThat(session.getDate()).isNull();
        assertThat(session.getDescription()).isNull();
        assertThat(session.getTeacher()).isNull();
        assertThat(session.getUsers()).isNull();
        assertThat(session.getCreatedAt()).isNull();
        assertThat(session.getUpdatedAt()).isNull();
    }

    @Test
    void givenAllArgs_whenCallIsMadeToAllArgsConstructor_thenInstanceIsConstructedAndGettersReturnSetData() {
        session = new Session(1L, "Session", nowDate, "Une session", teacher, List.of(), nowLocalDateTime, nowLocalDateTime);

        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Session");
        assertThat(session.getDate()).isEqualTo(nowDate);
        assertThat(session.getDescription()).isEqualTo("Une session");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).isEmpty();
        assertThat(session.getCreatedAt()).isEqualTo(nowLocalDateTime);
        assertThat(session.getUpdatedAt()).isEqualTo(nowLocalDateTime);
    }

    @Test
    void givenAllFieldsValuesAreSet_whenCallIsMadeToGetters_thenReturnsCorrectValues() {
        var nowDate = new Date();
        var nowLocalDateTime = LocalDateTime.now();

        var otherTeacher = Teacher.builder()
                .id(2L)
                .lastName("Garrison")
                .firstName("Herbert")
                .createdAt(nowLocalDateTime)
                .updatedAt(nowLocalDateTime)
                .build();

        session.setId(2L);
        session.setName("Session 2");
        session.setDate(nowDate);
        session.setDescription("Une autre session");
        session.setTeacher(otherTeacher);
        session.setUsers(List.of());
        session.setCreatedAt(nowLocalDateTime);
        session.setUpdatedAt(nowLocalDateTime);

        assertThat(session.getId()).isEqualTo(2L);
        assertThat(session.getName()).isEqualTo("Session 2");
        assertThat(session.getDate()).isEqualTo(nowDate);
        assertThat(session.getDescription()).isEqualTo("Une autre session");
        assertThat(session.getTeacher()).isEqualTo(otherTeacher);
        assertThat(session.getUsers()).isEmpty();
        assertThat(session.getCreatedAt()).isEqualTo(nowLocalDateTime);
        assertThat(session.getUpdatedAt()).isEqualTo(nowLocalDateTime);
    }

    @Test
    void givenTwoSessionsWithSameId_whenCallIsMadeToEqualsAndHashCode_thenEqual() {
        var session1 = session;

        var nowDate = new Date();
        var nowLocalDateTime = LocalDateTime.now();
        var otherTeacher = Teacher.builder()
                .id(2L)
                .lastName("Garrison")
                .firstName("Herbert")
                .createdAt(nowLocalDateTime)
                .updatedAt(nowLocalDateTime)
                .build();
        var session2 = new Session(1L, "Session 2", nowDate, "Une autre session", otherTeacher, List.of(), nowLocalDateTime, nowLocalDateTime);

        assertThat(session1).isEqualTo(session2);
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
    }

    @Test
    void givenTwoSessionsWithDifferentId_whenCallIsMadeToEqualsAndHashCode_thenNotEqual() {
        var session1 = session;

        var nowDate = new Date();
        var nowLocalDateTime = LocalDateTime.now();
        var otherTeacher = Teacher.builder()
                .id(2L)
                .lastName("Garrison")
                .firstName("Herbert")
                .createdAt(nowLocalDateTime)
                .updatedAt(nowLocalDateTime)
                .build();
        var session2 = new Session(2L, "Session 2", nowDate, "Une autre session", otherTeacher, List.of(), nowLocalDateTime, nowLocalDateTime);

        assertThat(session1).isNotEqualTo(session2);
        assertThat(session1.hashCode()).isNotEqualTo(session2.hashCode());
    }

    @Test
    void givenASession_whenCallIsMadeToToString_thenReturnIsNotNullAndContainsAllFieldsValues() {
        var toString = session.toString();

        assertThat(toString).isNotNull();
        assertThat(toString).contains("1");
        assertThat(toString).contains("Session");
        assertThat(toString).contains(nowDate.toString());
        assertThat(toString).contains("Une session");
        assertThat(toString).contains(teacher.toString());
        assertThat(toString).contains(nowLocalDateTime.toString());
    }
}