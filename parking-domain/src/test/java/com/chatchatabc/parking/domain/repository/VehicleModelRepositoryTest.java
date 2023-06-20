package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleModelRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Test
    void testFindByModelUuid_ShouldReturnVehicleType() {
        String modelUuid = "1debf602-68d7-4f79-8f8d-67bcb5b6c5df";
        assertThat(vehicleModelRepository.findByModelUuid(modelUuid)).isPresent();
    }

    @Test
    void testFindByModelUuid_ShouldBeEmpty() {
        String modelUuid = "non-existent-uuid";
        assertThat(vehicleModelRepository.findByModelUuid(modelUuid)).isEmpty();
    }

    @Test
    void testFindAllByBrandUuid_ShouldReturnGreaterThan0() {
        PageRequest pr = PageRequest.of(0, 10);
        String brandUuid = "8b06ff3f-6bef-434f-8b51-638c8ba30983";
        assertThat(vehicleModelRepository.findAllByBrandUuid(brandUuid, pr).getTotalElements()).isGreaterThan(0L);
    }

    @Test
    void testFindAllByBrandUuid_ShouldReturn0() {
        PageRequest pr = PageRequest.of(0, 10);
        String brandUuid = "non-existent-uuid";
        assertThat(vehicleModelRepository.findAllByBrandUuid(brandUuid, pr).getTotalElements()).isEqualTo(0L);
    }
}