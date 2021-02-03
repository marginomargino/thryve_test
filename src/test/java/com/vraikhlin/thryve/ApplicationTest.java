package com.vraikhlin.thryve;

import com.vraikhlin.thryve.helper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
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