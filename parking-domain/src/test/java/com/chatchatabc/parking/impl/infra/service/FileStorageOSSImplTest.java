package com.chatchatabc.parking.impl.infra.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;

public class FileStorageOSSImplTest extends TestContainersBaseTest {
    @Autowired
    private FileStorageOSSImpl fileStorageOSS;

    @Test
    public void testHealth() {
        final Health health = fileStorageOSS.health();
        assertThat(health.getStatus().getCode()).isEqualTo(Status.UP.getCode());
    }
}
