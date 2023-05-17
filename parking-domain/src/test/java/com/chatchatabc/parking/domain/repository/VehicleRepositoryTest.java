package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class VehicleRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    public void testFindByVehicleUuidAndOwner_ShouldReturnMatchingVehicleWhenExists() {
        String vehicleUuid = "2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9";
        Long ownerId = 5L;

        Optional<Vehicle> vehicleOptional = vehicleRepository.findByVehicleUuidAndOwner(vehicleUuid, ownerId);

        assertTrue(vehicleOptional.isPresent());
        assertEquals("2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9", vehicleOptional.get().getVehicleUuid());
        assertEquals("Lightning McQueen", vehicleOptional.get().getName());
        assertEquals("ASD1234", vehicleOptional.get().getPlateNumber());
        assertEquals(0, vehicleOptional.get().getType());
        assertEquals(ownerId, vehicleOptional.get().getOwner());
    }

    @Test
    public void testFindByVehicleUuidAndOwner_ShouldReturnEmptyOptionalWhenVehicleDoesNotExist() {
        String vehicleUuid = "non-existent-uuid";
        Long ownerId = 5L;

        Optional<Vehicle> vehicleOptional = vehicleRepository.findByVehicleUuidAndOwner(vehicleUuid, ownerId);

        assertTrue(vehicleOptional.isEmpty());
    }

    @Test
    public void testFindByVehicleUuid_ShouldReturnMatchingVehicleWhenExists() {
        String vehicleUuid = "2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9";

        Optional<Vehicle> vehicleOptional = vehicleRepository.findByVehicleUuid(vehicleUuid);

        assertTrue(vehicleOptional.isPresent());
        Vehicle vehicle = vehicleOptional.get();
        assertEquals(vehicleUuid, vehicle.getVehicleUuid());
        assertEquals("Lightning McQueen", vehicle.getName());
        assertEquals("ASD1234", vehicle.getPlateNumber());
        assertEquals(0, vehicle.getType());
        assertEquals(5L, vehicle.getOwner());
    }

    @Test
    public void testFindByVehicleUuid_ShouldReturnEmptyOptionalWhenVehicleDoesNotExist() {
        String vehicleUuid = "non-existent-uuid";

        Optional<Vehicle> vehicleOptional = vehicleRepository.findByVehicleUuid(vehicleUuid);

        assertTrue(vehicleOptional.isEmpty());
    }

    @Test
    void findAllByMember() {
    }
}