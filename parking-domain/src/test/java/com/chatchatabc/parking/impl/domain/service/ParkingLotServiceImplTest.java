package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingLotServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private ParkingLotServiceImpl parkingLotService;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    void testRegisterParkingLot_WhenOwnerHasNoParkingLot_ShouldRegisterParkingLot() {
        Long longOwnerId = 2L;
        String ownerId = "b69bf0f3-3a3a-4079-94e6-776f747fd3de";
        assertThat(parkingLotRepository.findByOwnerUuid(ownerId)).isEmpty();

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setOwner(longOwnerId);
        parkingLot.setName("Test Parking Lot");
        parkingLot.setLatitude(1.0);
        parkingLot.setLongitude(1.0);
        parkingLot.setAddress("Test Address");
        parkingLot.setDescription("Test Description");
        parkingLot.setCapacity(100);
        parkingLot.setAvailableSlots(100);
        parkingLot.setStatus(0);
        parkingLot.setBusinessHoursStart(LocalDateTime.now());
        parkingLot.setBusinessHoursEnd(LocalDateTime.now());

        parkingLotService.saveParkingLot(parkingLot);

        assertThat(parkingLotRepository.findByOwnerUuid(ownerId)).isPresent();
    }

    @Test
    void testRegisterParkingLot_WhenOwnerHasParkingLot_ShouldNotRegisterParkingLot() {
        Long longOwnerId = 7L;
        String ownerId = "10f1ac81-4eff-4fa2-855e-84b03a348623";
        assertThat(parkingLotRepository.findByOwnerUuid(ownerId)).isPresent();

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setOwner(longOwnerId);
        parkingLot.setName("Test Parking Lot");
        parkingLot.setLatitude(1.0);
        parkingLot.setLongitude(1.0);
        parkingLot.setAddress("Test Address");
        parkingLot.setDescription("Test Description");
        parkingLot.setCapacity(100);
        parkingLot.setAvailableSlots(100);
        parkingLot.setStatus(0);
        parkingLot.setBusinessHoursStart(LocalDateTime.now());
        parkingLot.setBusinessHoursEnd(LocalDateTime.now());

        assertThrows(Exception.class, () -> parkingLotService.saveParkingLot(parkingLot));
    }

    @Test
    void updateParkingLot() {
    }

    @Test
    void verifyParkingLot() {
    }
}