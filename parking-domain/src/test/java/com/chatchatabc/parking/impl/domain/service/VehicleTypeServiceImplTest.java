package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.VehicleType;
import com.chatchatabc.parking.domain.repository.VehicleTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class VehicleTypeServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private VehicleTypeServiceImpl vehicleTypeService;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Test
    void testSaveVehicleType_ShouldBeSuccessful() {
        // Given
        VehicleType vehicleType = new VehicleType();
        vehicleType.setName("Car");
        vehicleType.setStatus(VehicleType.VehicleTypeStatus.ACTIVE);
        vehicleType.setCreatedBy(1L);
        Long count = vehicleTypeRepository.count();

        // When
        vehicleTypeService.saveVehicleType(vehicleType);

        // Then
        assertThat(vehicleTypeRepository.count()).isGreaterThan(count);
    }
}