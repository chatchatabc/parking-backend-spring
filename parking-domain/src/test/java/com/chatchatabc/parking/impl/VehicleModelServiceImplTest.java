package com.chatchatabc.parking.impl;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.VehicleModel;
import com.chatchatabc.parking.domain.repository.VehicleModelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleModelServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private VehicleModelServiceImpl vehicleModelService;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Test
    void testSaveVehicleModel_ShouldBeSuccessful() {
        // Given
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setName("Camry");
        vehicleModel.setStatus(VehicleModel.VehicleModelStatus.ACTIVE);
        vehicleModel.setCreatedBy(1L);
        vehicleModel.setBrandUuid("8b06ff3f-6bef-434f-8b51-638c8ba30983");
        Long count = vehicleModelRepository.count();

        // When
        vehicleModelService.saveVehicleModel(vehicleModel);

        // Then
        assertThat(vehicleModelRepository.count()).isGreaterThan(count);
    }
}