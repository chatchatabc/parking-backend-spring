package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleTypeRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Test
    void testFindAllByStatus_ShouldReturnGreaterThan0() {
        Integer status = VehicleType.VehicleTypeStatus.ACTIVE;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(vehicleTypeRepository.findAllByStatus(status, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByStatus_WithNonExistingStatus_ShouldReturn0() {
        Integer status = VehicleType.VehicleTypeStatus.INACTIVE;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(vehicleTypeRepository.findAllByStatus(status, pr).getTotalElements()).isEqualTo(0);
    }

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