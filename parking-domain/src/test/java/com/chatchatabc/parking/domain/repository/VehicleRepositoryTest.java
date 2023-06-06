package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void testFindAllByOwner_ShouldReturnMatchingVehiclesWhenExist() {
        Long ownerId = 5L;

        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> vehiclePage = vehicleRepository.findAllByOwner(ownerId, pageable);

        assertEquals(3, vehiclePage.getTotalElements());
    }

    @Test
    public void testFindAllByOwner_ShouldReturnEmptyPageWhenNoVehiclesExist() {
        Long ownerId = 10L;

        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> vehiclePage = vehicleRepository.findAllByOwner(ownerId, pageable);

        assertEquals(0, vehiclePage.getTotalElements());
    }

    @Test
    void testFindAllByUser_ShouldReturnGreaterThan0() {
        String userUuid = "b0c50381-d0bd-455d-9e46-2b0bd599320b";
        PageRequest pageable = PageRequest.of(0, 10);
        assertThat(vehicleRepository.findAllByUser(userUuid, pageable).getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByUser_ShouldReturn0() {
        String userUuid = "ec4af6e9-ec57-434d-990d-ae83d9459a31";
        PageRequest pageable = PageRequest.of(0, 10);
        assertThat(vehicleRepository.findAllByUser(userUuid, pageable).getNumberOfElements()).isEqualTo(0);
    }

    @Test
    void testFindByPlateNumber_ShouldReturnVehicle() {
        String plateNumber = "ASD1234";
        assertThat(vehicleRepository.findByPlateNumber(plateNumber).orElseThrow().getPlateNumber()).isEqualTo(plateNumber);
    }

    @Test
    void testFindByPlateNumber_ShouldReturnEmptyOptional() {
        String plateNumber = "non-existent-plate-number";
        assertThat(vehicleRepository.findByPlateNumber(plateNumber).isEmpty()).isTrue();
    }
}