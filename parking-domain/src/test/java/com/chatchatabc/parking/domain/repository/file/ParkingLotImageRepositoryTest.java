package com.chatchatabc.parking.domain.repository.file;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.file.CloudFile;
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
        assertThat(parkingLotImageRepository.findAllByParkingLotAndStatus(parkingLotId, CloudFile.ACTIVE, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByParkingLotAndStatus_ShouldReturn0() {
        Long parkingLotId = 1L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(parkingLotImageRepository.findAllByParkingLotAndStatus(parkingLotId, CloudFile.ACTIVE, pr).getTotalElements()).isEqualTo(0);
    }

    @Test
    void testFindByIdAndStatus_WhenParkingLotImageExist_ShouldReturnParkingLotImage() {
        String imageId = "5810590b-6a23-481f-888b-0919fad5864d";
        int status = CloudFile.ACTIVE;
        assertThat(parkingLotImageRepository.findByIdAndStatus(imageId, status)).isPresent();
    }

    @Test
    void testFindByIdAndStatus_WhenParkingLotImageDoesNotExist_ShouldReturnEmpty() {
        String imageId = "nonexistent_id";
        int status = CloudFile.ACTIVE;
        assertThat(parkingLotImageRepository.findByIdAndStatus(imageId, status)).isEmpty();
    }

    @Test
    void testFindAllParkingLotKeysByParkingLotAndStatus_ShouldReturnGreaterThan0() {
        Long parkingLotId = 2L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(parkingLotImageRepository.findAllParkingLotKeysByParkingLotAndStatus(parkingLotId, CloudFile.ACTIVE, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllParkingLotKeysByParkingLotAndStatus_ShouldReturn0() {
        Long parkingLotId = 100L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(parkingLotImageRepository.findAllParkingLotKeysByParkingLotAndStatus(parkingLotId, CloudFile.ACTIVE, pr).getTotalElements()).isEqualTo(0);
    }
}