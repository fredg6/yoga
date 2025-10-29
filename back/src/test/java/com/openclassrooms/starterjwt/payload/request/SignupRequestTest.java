package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {
    private SignupRequest signupRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Test");
        signupRequest.setPassword("test!1234");

        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void givenAValidSignupRequest_whenCallIsMadeToValidate_thenViolationsIsEmpty() {
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenASignupRequestWithBlankEmail_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsEmailViolation() {
        signupRequest.setEmail("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void givenASignupRequestWithNullEmail_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsEmailViolation() {
        signupRequest.setEmail(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void givenASignupRequestWithInvalidFormatEmail_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsEmailViolation() {
        signupRequest.setEmail("test-test");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void givenASignupRequestWithTooLongEmail_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsEmailViolation() {
        signupRequest.setEmail("test".repeat(15) + "@test.com");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void givenASignupRequestWithBlankFirstName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsFirstNameViolation() {
        signupRequest.setFirstName("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void givenASignupRequestWithNullFirstName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsFirstNameViolation() {
        signupRequest.setFirstName(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void givenASignupRequestWithTooShortFirstName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsFirstNameViolation() {
        signupRequest.setFirstName("Te");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void givenASignupRequestWithTooLongFirstName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsFirstNameViolation() {
        signupRequest.setFirstName("Test".repeat(10));

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void givenASignupRequestWithBlankLastName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsLastNameViolation() {
        signupRequest.setLastName("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void givenASignupRequestWithNullLastName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsLastNameViolation() {
        signupRequest.setLastName(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void givenASignupRequestWithTooShortLastName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsLastNameViolation() {
        signupRequest.setLastName("Te");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void givenASignupRequestWithTooLongLastName_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsLastNameViolation() {
        signupRequest.setLastName("Test".repeat(10));

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void givenASignupRequestWithBlankPassword_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsPasswordViolation() {
        signupRequest.setPassword("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void givenASignupRequestWithNullPassword_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsPasswordViolation() {
        signupRequest.setPassword(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void givenASignupRequestWithTooShortPassword_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsPasswordViolation() {
        signupRequest.setPassword("test!");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void givenASignupRequestWithTooLongPassword_whenCallIsMadeToValidate_thenViolationsIsNotEmptyAndContainsPasswordViolation() {
        signupRequest.setPassword("test!1234".repeat(5));

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void whenCallIsMadeToGetters_thenReturnsSetData() {
        assertEquals("test@test.com", signupRequest.getEmail());
        assertEquals("Test", signupRequest.getFirstName());
        assertEquals("Test", signupRequest.getLastName());
        assertEquals("test!1234", signupRequest.getPassword());
    }

    @Test
    void givenTwoEqualSignupRequests_whenCallIsMadeToEqualsAndHashCode_thenTrue() {
        var signupRequest1 = signupRequest;

        var signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@test.com");
        signupRequest2.setFirstName("Test");
        signupRequest2.setLastName("Test");
        signupRequest2.setPassword("test!1234");

        assertEquals(signupRequest1, signupRequest2);
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode());
    }

    @Test
    void givenASignupRequest_whenCallIsMadeToToString_thenReturnIsNotNullAndContainsAllFieldsValues() {
        var toString = signupRequest.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("test@test.com"));
        assertTrue(toString.contains("Test"));
        assertTrue(toString.contains("Test"));
        assertTrue(toString.contains("test!1234"));
    }
}