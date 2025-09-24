package com.khoahd7621.youngblack;

import com.khoahd7621.youngblack.controllers.AuthController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTests {

    @Autowired
    private AuthController authController;

    @Test
    void contextLoads() {
        Assertions.assertThat(authController).isNotNull();
    }

}
