package fr.alten.test_back.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
@TestConfiguration
@Profile("test")
public class TestcontainersConfig {

    /**
     * Mysql container.
     */
    @Container
    final static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    static {
        mysql.start();
    }

    /**
     * Define test data source.
     * @return Generated data source.
     */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url(mysql.getJdbcUrl())
            .username(mysql.getUsername())
            .password(mysql.getPassword())
            .driverClassName(mysql.getDriverClassName())
            .build();

    }

    /**
     * Create database initialization trigger test.
     * @param initializer DB initialization task.
     * @return Database initialization trigger test.
     */
    @Bean
    public DatabaseInitTrigger databaseInitTrigger(DatabaseInitializer initializer) {
        return new DatabaseInitTrigger(initializer);
    }

}
