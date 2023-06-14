package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class JeepneyRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private JeepneyRepository jeepneyRepository;

    @Test
    void testFindByJeepneyUuid_WhenJeepneyUuidExist_ShouldReturnJeepney() {
        String jeepneyUuid = "8821fff6-b725-454c-bdd3-0674b313ba45";
        assertThat(jeepneyRepository.findByJeepneyUuid(jeepneyUuid)).isPresent();
    }

    @Test
    void testFindByJeepneyUuid_WhenJeepneyUuidDoesNotExist_ShouldReturnEmpty() {
        String jeepneyUuid = "non-existent-uuid";
        assertThat(jeepneyRepository.findByJeepneyUuid(jeepneyUuid)).isEmpty();
    }

}