package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ParkingLotRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    void testFindByParkingLotUuid_ParkingLotIsFound() {
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        assertThat(parkingLotRepository.findByParkingLotUuid(parkingLotUuid)).isPresent();
    }

    @Test
    void testFindByParkingLotUuid_ParkingLotIsNotFound() {
        String parkingLotUuid = "non-existent-parking-lot-uuid";
        assertThat(parkingLotRepository.findByParkingLotUuid(parkingLotUuid)).isEmpty();
    }

    @Test
    void testFindByOwner_ParkingLotIsFound() {
        Long ownerId = 7L;
        assertThat(parkingLotRepository.findByOwner(ownerId)).isPresent();
    }

    @Test
    void testFindByOwner_ParkingLotIsNotFound() {
        Long ownerId = 0L;
        assertThat(parkingLotRepository.findByOwner(ownerId)).isEmpty();
    }


    @Test
    void testFindByOwnerUuid_ParkingLotIsFound() {
        String ownerUuid = "10f1ac81-4eff-4fa2-855e-84b03a348623";
        assertThat(parkingLotRepository.findByOwnerUuid(ownerUuid)).isPresent();
    }

    @Test
    void testFindByOwnerUuid_ParkingLotIsNotFound() {
        String ownerUuid = "non-existent-owner-uuid";
        assertThat(parkingLotRepository.findByOwnerUuid(ownerUuid)).isEmpty();
    }

    @Test
    void testFindByDistance_ParkingLotsAreFound() {
        double latitude = 7.100576;
        double longitude = 125.630625;
        double radius = 0.1;
        assertThat(parkingLotRepository.findByDistance(latitude, longitude, radius).size()).isGreaterThan(0);
    }

    @Test
    void testFindByDistance_ParkingLotsNotFound() {
        // Function works, but there are no parking lots in the specified radius
        double latitude = 0.0;
        double longitude = 0.0;
        double radius = 0.1;
        assertThat(parkingLotRepository.findByDistance(latitude, longitude, radius)).isEmpty();
    }

    @Test
    void testCountVerified() {
        assertThat(parkingLotRepository.countVerified()).isGreaterThan(0L);
    }
}