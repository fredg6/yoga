package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.FilterChain;

import static org.mockito.Mockito.*;

@SpringBootTest
class AuthTokenFilterTest {
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @SneakyThrows
    void givenHttpRequestWithValidJwt_whenDoFilterIsCalled_thenOK() {
        var spiedAuthTokenFilter = spy(new AuthTokenFilter());

        var userDetails = UserDetailsImpl.builder().username("test@test.com").build();
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        var req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " + jwtUtils.generateJwtToken(authentication));
        var res = new MockHttpServletResponse();
        var chain = mock(FilterChain.class);

        spiedAuthTokenFilter.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
    }
}