package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.VehicleBrand;
import com.chatchatabc.parking.domain.repository.VehicleBrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleBrandServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;

    @Autowired
    private VehicleBrandServiceImpl vehicleBrandService;

    @Test
    void testSaveVehicleBrand_ShouldBeSuccessful() {
        // Given
        VehicleBrand vehicleBrand = new VehicleBrand();
        vehicleBrand.setName("Toyota");
        vehicleBrand.setStatus(VehicleBrand.VehicleBrandStatus.ACTIVE);
        vehicleBrand.setCreatedBy(1L);
        Long count = vehicleBrandRepository.count();

        // When
        vehicleBrandService.saveVehicleBrand(vehicleBrand);

        // Then
        assertThat(vehicleBrandRepository.count()).isGreaterThan(count);

    }
}