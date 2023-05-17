package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class VehicleRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    public void testFindByVehicleUuidAndOwner_ShouldReturnMatchingVehicleWhenExists() {
        // Given
        String vehicleUuid = "2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9";
        Long ownerId = 5L;

        // When
        Optional<Vehicle> vehicleOptional = vehicleRepository.findByVehicleUuidAndOwner(vehicleUuid, ownerId);

        // Then
        assertTrue(vehicleOptional.isPresent());
        assertEquals("2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9", vehicleOptional.get().getVehicleUuid());
        assertEquals("Lightning McQueen", vehicleOptional.get().getName());
        assertEquals("ASD1234", vehicleOptional.get().getPlateNumber());
        assertEquals(0, vehicleOptional.get().getType());
        assertEquals(ownerId, vehicleOptional.get().getOwner());
    }

    @Test
    void findByVehicleUuid() {
    }

    @Test
    void findAllByOwner() {
    }

    @Test
    void findAllByMember() {
    }
}