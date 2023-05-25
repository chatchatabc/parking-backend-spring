package com.chatchatabc.parking.domain.repository.file;

import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingLotImageRepository extends JpaRepository<ParkingLotImage, String> {

    /**
     * Find all parking lot images by parking lot id and status value
     *
     * @param parkingLotId the id of the parking lot
     * @param status       the status
     * @param pageable     the pageable
     * @return the page of parking lot images
     */
    @Query("SELECT pli FROM ParkingLotImage pli WHERE pli.parkingLot = :parkingLotId AND pli.cloudFile.status = :status")
    Page<ParkingLotImage> findAllByParkingLotAndStatus(Long parkingLotId, int status, Pageable pageable);

    /**
     * Find all parking lot images by id and status value
     *
     * @param id     the parking lot id
     * @param status the status
     * @return the parking lot image
     */
    @Query("SELECT pli FROM ParkingLotImage pli WHERE pli.id = :id AND pli.cloudFile.status = :status")
    Optional<ParkingLotImage> findByIdAndStatus(String id, int status);
}
