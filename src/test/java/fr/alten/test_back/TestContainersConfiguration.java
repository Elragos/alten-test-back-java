package fr.alten.test_back;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.time.Duration;

/**
 * Test configuration database.
 * @author amarechal
 */
@TestConfiguration
public class TestContainersConfiguration {

    /**
     * Used MySQL container.
     */
    private static final MySQLContainer<?> MYSQL_CONTAINER;

    /**
     * Create and launch container.
     */
    static {
        MYSQL_CONTAINER = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password")
            .withMinimumRunningDuration(Duration.ofSeconds(10));
        MYSQL_CONTAINER.start();
    }

    /**
     * Define data source.
     *
     * @return Defined data source.
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(MYSQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(MYSQL_CONTAINER.getUsername());
        dataSource.setPassword(MYSQL_CONTAINER.getPassword());
        return dataSource;
    }
}
