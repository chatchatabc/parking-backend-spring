package com.chatchatabc.parking;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.spring.api.DBRider;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

@Import(TestContainersConfiguration.class)
@Testcontainers
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, columnSensing = true, schema = "public" )
public abstract class TestContainersBaseTest extends SpringBootBaseTest {
}
