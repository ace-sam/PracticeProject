package com.practice.redo.practice;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


public class TestContainerTest extends AbstractTestContainerUnitTest{

//    @Container
//    private static final PostgreSQLContainer<?> postgresSQLContainer =
//            new PostgreSQLContainer<>("postgres:latest")
//                    .withDatabaseName("practice-dao-unit-test")
//                    .withUsername("practiceUser")
//                    .withPassword("password");
//
//    @DynamicPropertySource
//    private static void registerDataSourceProperties(DynamicPropertyRegistry registry){
//        registry.add(
//                "spring.datasource.url",
//                postgresSQLContainer::getJdbcUrl
//        );
//
//        registry.add(
//                "spring.datasource.username",
//                postgresSQLContainer::getUsername
//        );
//
//        registry.add(
//                "spring.datasource.password",
//                postgresSQLContainer::getPassword
//        );
//    }



//    @Test
//    void canApplyDbMigrationWithFlyway(){
//        Flyway flyway= Flyway.configure().dataSource(
//                postgresSQLContainer.getJdbcUrl(),
//                postgresSQLContainer.getUsername(),
//                postgresSQLContainer.getPassword()
//        ).load();
//        flyway.migrate();
//        //System.out.println(flyway);
//    }


    @Test
    void canStartPostgresDB() {

        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
