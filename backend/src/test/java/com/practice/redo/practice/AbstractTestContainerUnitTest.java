package com.practice.redo.practice;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestContainerUnitTest {


    protected Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {

        Flyway flyway= Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();

    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer= new PostgreSQLContainer<>(
            "postgres:latest")
            .withDatabaseName("dao-tests-db")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    private static void registryDataSource(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add(
                "spring.datasource.url", postgreSQLContainer::getJdbcUrl
        );

        dynamicPropertyRegistry.add(
                "spring.datasource.username", postgreSQLContainer::getUsername
        );

        dynamicPropertyRegistry.add(
                "spring.datasource.password", postgreSQLContainer::getPassword
        );
    }

    protected static DataSource getDataSource(){
        DataSourceBuilder builder=DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword());
        return builder.build();
    }

}
