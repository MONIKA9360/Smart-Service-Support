package com.example.support;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SupportApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the Spring Boot ApplicationContext loads successfully
        // with all JPA entities, repositories, mappers, and configurations.
    }
}
