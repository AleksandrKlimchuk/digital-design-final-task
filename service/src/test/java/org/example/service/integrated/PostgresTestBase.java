package org.example.service.integrated;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PostgresTestBase {

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    public static void globalInit() {
        postgresContainer = postgresContainer.withReuse(true);
        postgresContainer.start();
    }

    @AfterAll
    public static void globalDestroy() {
        postgresContainer.stop();
    }

    @DynamicPropertySource
    public static void registry(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
}
