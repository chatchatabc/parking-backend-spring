package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleTypeRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Test
    void testFindByTypeUuid_ShouldReturnVehicleType() {
        String typeUuid = "a22a75df-9c4c-49b8-80f6-139d5df83dca";
        assertThat(vehicleTypeRepository.findByTypeUuid(typeUuid)).isPresent();
    }

    @Test
    void testFindByTypeUuid_ShouldBeEmpty() {
        String typeUuid = "non-existent-uuid";
        assertThat(vehicleTypeRepository.findByTypeUuid(typeUuid)).isEmpty();
    }
}