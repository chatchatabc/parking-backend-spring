package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleBrandRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;

    @Test
    void testFindByBrandUuid_ShouldReturnVehicleBrand() {
        String vehicleBrandUuid = "8b06ff3f-6bef-434f-8b51-638c8ba30983";
        assertThat(vehicleBrandRepository.findByBrandUuid(vehicleBrandUuid)).isPresent();
    }

    @Test
    void testFindByBrandUuid_WithInvalidUuid_ShouldReturnEmpty() {
        String vehicleBrandUuid = "non-existent-uuid";
        assertThat(vehicleBrandRepository.findByBrandUuid(vehicleBrandUuid)).isEmpty();
    }
}