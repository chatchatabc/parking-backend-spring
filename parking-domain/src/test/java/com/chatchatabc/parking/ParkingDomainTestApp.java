package com.chatchatabc.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParkingDomainTestApp {
    public static void main(String[] args) {
        SpringApplication
                .from(ParkingDomainTestApp::main)
                .with(TestContainersConfiguration.class)
                .run(args);
    }
}
