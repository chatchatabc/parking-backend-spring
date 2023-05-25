package com.chatchatabc.parking.domain.repository.file;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ParkingLotImageRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private ParkingLotImageRepository parkingLotImageRepository;

    @Test
    void testFindAllByParkingLotAndStatus_ShouldReturnGreaterThan0() {
        Long parkingLotId = 2L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(parkingLotImageRepository.findAllByParkingLotAndStatus(parkingLotId, 0, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByParkingLotAndStatus_ShouldReturn0() {
        Long parkingLotId = 1L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(parkingLotImageRepository.findAllByParkingLotAndStatus(parkingLotId, 0, pr).getTotalElements()).isEqualTo(0);
    }

    @Test
    void testFindByIdAndStatus() {
    }
}