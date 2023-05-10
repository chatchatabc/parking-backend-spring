package com.chatchatabc.parking;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@SuppressWarnings("resource")
@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfiguration {

    /**
     * Test PostgreSQL container
     *
     * @return PostgreSQLContainer
     */
    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:15.2");
    }

    @Bean
    @ServiceConnection("redis")
    public GenericContainer<?> redisContainer() {
        return new GenericContainer<>("redis:7.0.10")
                .withExposedPorts(6379);
    }

    @Bean
    public GenericContainer<?> natsContainer(DynamicPropertyRegistry properties) {
        GenericContainer<?> natsContainer = new GenericContainer<>("nats:2.9.16").withExposedPorts(4222);
        properties.add("spring.nats.uri", () -> "nats://" + natsContainer.getHost() + ":" + natsContainer.getMappedPort(4222));
        return natsContainer;
    }

}
