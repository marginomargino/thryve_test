package com.vraikhlin.thryve;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("Application test")
class ApplicationTest {

    @Autowired
    Application application;

    @Test
    @DisplayName("Application Context successfully loads")
    void contextLoads() {
        assertNotNull(application);
    }
}