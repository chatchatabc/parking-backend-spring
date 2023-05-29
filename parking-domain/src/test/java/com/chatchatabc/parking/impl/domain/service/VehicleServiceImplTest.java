package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Vehicle;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private VehicleServiceImpl vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void testRegisterVehicle_WhenVehicleDoesNotExist_ShouldRegisterVehicle() throws Exception {
        PageRequest pr = PageRequest.of(0, 10);
        Long currentCount = vehicleRepository.findAllByUser("b0c50381-d0bd-455d-9e46-2b0bd599320b", pr).getTotalElements();
        vehicleService.registerVehicle("b0c50381-d0bd-455d-9e46-2b0bd599320b", "Lightning DaQueen", "ASD12345", 0);
        assertThat(vehicleRepository.findAllByUser("b0c50381-d0bd-455d-9e46-2b0bd599320b", pr).getTotalElements()).isGreaterThan(currentCount);
    }

    @Test
    void testRegisterVehicle_WhenVehicleAlreadyExists_ShouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> vehicleService.registerVehicle("b0c50381-d0bd-455d-9e46-2b0bd599320b", "Lightning McQueen", "ASD1234", 0));
    }

    @Test
    void testUpdateVehicle_WhenVehicleExist_ShouldUpdateVehicle() {
        Vehicle updatedVehicle = vehicleRepository.findById(1L).orElseThrow();
        updatedVehicle.setName("Hyperion");
        vehicleService.updateVehicle(updatedVehicle);
        assertThat(vehicleRepository.findById(1L).orElseThrow().getName()).isEqualTo("Hyperion");
    }

    @Test
        // TODO:
    void addUserToVehicle() {
    }

    @Test
        // TODO:
    void removeUserFromVehicle() {
    }
}