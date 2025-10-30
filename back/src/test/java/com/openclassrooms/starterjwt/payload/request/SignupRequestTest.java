package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {
    private SignupRequest signupRequest;

    @BeforeEach
    void init() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Testt");
        signupRequest.setPassword("test!1234");
    }

    @Test
    void whenCallIsMadeToGetters_thenReturnsSetData() {
        assertThat(signupRequest.getEmail()).isEqualTo("test@test.com");
        assertThat(signupRequest.getFirstName()).isEqualTo("Test");
        assertThat(signupRequest.getLastName()).isEqualTo("Testt");
        assertThat(signupRequest.getPassword()).isEqualTo("test!1234");
    }

    @Test
    void givenTwoEqualSignupRequests_whenCallIsMadeToEqualsAndHashCode_thenEqual() {
        var signupRequest1 = signupRequest;

        var signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@test.com");
        signupRequest2.setFirstName("Test");
        signupRequest2.setLastName("Testt");
        signupRequest2.setPassword("test!1234");

        assertThat(signupRequest1).isEqualTo(signupRequest2);
        assertThat(signupRequest1.hashCode()).isEqualTo(signupRequest2.hashCode());
    }

    @Test
    void givenASignupRequest_whenCallIsMadeToToString_thenReturnIsNotNullAndContainsAllFieldsValues() {
        var toString = signupRequest.toString();
        assertThat(toString).isNotNull();
        assertThat(toString).contains("test@test.com");
        assertThat(toString).contains("Test");
        assertThat(toString).contains("Testt");
        assertThat(toString).contains("test!1234");
    }
}