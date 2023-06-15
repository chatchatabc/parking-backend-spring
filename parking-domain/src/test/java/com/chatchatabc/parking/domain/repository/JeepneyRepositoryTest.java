package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

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

    @Test
    void testFindByPlateNumber_WhenPlateNumberExist_ShouldReturnJeepney() {
        String plateNumber = "AAA-1234";
        assertThat(jeepneyRepository.findByPlateNumber(plateNumber)).isPresent();
    }

    @Test
    void testFindByPlateNumber_WhenPlateNumberDoesNotExist_ShouldReturnEmpty() {
        String plateNumber = "non-existent-plate-number";
        assertThat(jeepneyRepository.findByPlateNumber(plateNumber)).isEmpty();
    }

    @Test
    void testFindByName_WhenNameExists_ShouldReturnJeepney() {
        String name = "Test";
        assertThat(jeepneyRepository.findByName(name)).isPresent();
    }

    @Test
    void testFindByName_WhenNameDoesNotExist_ShouldReturnEmpty() {
        String name = "non-existent-name";
        assertThat(jeepneyRepository.findByName(name)).isEmpty();
    }

    @Test
    void testFindAllByRouteUuid_ShouldReturnJeepneys() {
        String routeUuid = "8821fff6-b725-454c-bdd3-0674b313ba45";
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(jeepneyRepository.findAllByRouteUuid(routeUuid, pr)).isNotEmpty();
    }

    @Test
    void testFindAllByRouteUuid_WhenRouteUuidDoesNotExist_ShouldReturnEmpty() {
        String routeUuid = "non-existent-route-uuid";
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(jeepneyRepository.findAllByRouteUuid(routeUuid, pr)).isEmpty();
    }
}