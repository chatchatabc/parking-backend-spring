package com.chatchatabc.parking;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ParkingDomainTestApp.class)
@ActiveProfiles("test")
public abstract class SpringBootBaseTest {
}
