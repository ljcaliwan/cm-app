package dev.ljcaliwan.cmbackend;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContainerTest extends AbstractTestcontainers {

    @Test
     void canStartPostgresDB() {
        assertThat(postgreSqlContainer.isRunning()).isTrue();
        assertThat(postgreSqlContainer.isCreated()).isTrue();
    }
}
