package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void givenAnEmptyToken_whenValidateJwtTokenIsCalled_thenFalseIsReturned() {
        assertThat(jwtUtils.validateJwtToken("")).isFalse();
    }

    @Test
    void givenAMalformedToken_whenValidateJwtTokenIsCalled_thenFalseIsReturned() {
        assertThat(jwtUtils.validateJwtToken("qsdi oqiusdy qusyddè")).isFalse();
    }
}