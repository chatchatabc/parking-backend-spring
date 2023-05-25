package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
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
        Long currentCount = vehicleRepository.findAllByMember("b0c50381-d0bd-455d-9e46-2b0bd599320b", pr).getTotalElements();
        vehicleService.registerVehicle("b0c50381-d0bd-455d-9e46-2b0bd599320b", "Lightning DaQueen", "ASD12345", 0);
        assertThat(vehicleRepository.findAllByMember("b0c50381-d0bd-455d-9e46-2b0bd599320b", pr).getTotalElements()).isGreaterThan(currentCount);
    }

    @Test
    void updateVehicle() {
    }

    @Test
    void addMemberToVehicle() {
    }

    @Test
    void removeMemberFromVehicle() {
    }
}