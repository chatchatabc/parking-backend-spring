package com.chatchatabc.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TestContainersConfiguration.class, InfraConfiguration.class})
public class ParkingDomainTestApp {
    public static void main(String[] args) {
        SpringApplication.run(ParkingDomainTestApp.class, args);
    }
}
