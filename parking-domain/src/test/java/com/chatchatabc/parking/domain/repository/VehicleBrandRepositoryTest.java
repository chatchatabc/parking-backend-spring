package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.VehicleBrand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleBrandRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;

    @Test
    void testFindAllByStatus_ShouldReturnGreaterThan0() {
        PageRequest pr = PageRequest.of(0, 10);
        Integer status = VehicleBrand.VehicleBrandStatus.ACTIVE;
        assertThat(vehicleBrandRepository.findAllByStatus(status, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByStatus_WithNonExistingStatus_ShouldReturn0() {
        PageRequest pr = PageRequest.of(0, 10);
        Integer status = -1;
        assertThat(vehicleBrandRepository.findAllByStatus(status, pr).getTotalElements()).isEqualTo(0);
    }

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