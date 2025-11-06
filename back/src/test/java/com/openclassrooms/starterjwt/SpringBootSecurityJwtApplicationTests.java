package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {
    @Autowired
    private AuthController authController;

    @Test
    public void contextLoads() {
        SpringBootSecurityJwtApplication.main(new String[]{});

        assertThat(authController).isNotNull();
    }
}