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
        Long ownerId = 2L;
        assertThat(parkingLotRepository.findByOwner(ownerId)).isPresent();
    }

    @Test
    void testFindByOwner_ParkingLotIsNotFound() {
        Long ownerId = 0L;
        assertThat(parkingLotRepository.findByOwner(ownerId)).isEmpty();
    }

    @Test
    void findByDistance() {
    }

    @Test
    void findAllByOwner() {
    }

    @Test
    void findAllByOwnerAndStatus() {
    }

    @Test
    void countVerified() {
    }
}