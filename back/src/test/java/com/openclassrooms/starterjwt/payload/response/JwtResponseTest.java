package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtResponseTest {
    @Test
    void givenRequiredArgs_whenCallIsMadeToConstructor_thenInstanceIsConstructedAndDataSet() {
        var token = "jwt";
        var id = 1L;
        var username = "test@test.com";
        var firstName = "Test";
        var lastName = "Test";
        var admin = false;

        var jwtResponse = new JwtResponse(token, id, username, firstName, lastName, admin);

        assertThat(jwtResponse.getToken()).isEqualTo(token);
        assertThat(jwtResponse.getType()).isEqualTo("Bearer");
        assertThat(jwtResponse.getId()).isEqualTo(id);
        assertThat(jwtResponse.getUsername()).isEqualTo(username);
        assertThat(jwtResponse.getFirstName()).isEqualTo(firstName);
        assertThat(jwtResponse.getLastName()).isEqualTo(lastName);
        assertThat(jwtResponse.getAdmin()).isEqualTo(admin);
    }

    @Test
    void givenAllFieldsValues_whenCallsAreMadeToSetters_thenGettersReturnCorrectValues() {
        var token = "jwt";
        var id = 1L;
        var username = "test@test.com";
        var firstName = "Test";
        var lastName = "Test";
        var admin = false;

        var jwtResponse = new JwtResponse(null, null, null, null, null, null);
        jwtResponse.setToken(token);
        jwtResponse.setId(id);
        jwtResponse.setUsername(username);
        jwtResponse.setFirstName(firstName);
        jwtResponse.setLastName(lastName);
        jwtResponse.setAdmin(admin);

        assertThat(jwtResponse.getToken()).isEqualTo(token);
        assertThat(jwtResponse.getId()).isEqualTo(id);
        assertThat(jwtResponse.getUsername()).isEqualTo(username);
        assertThat(jwtResponse.getFirstName()).isEqualTo(firstName);
        assertThat(jwtResponse.getLastName()).isEqualTo(lastName);
        assertThat(jwtResponse.getAdmin()).isEqualTo(admin);
    }
}