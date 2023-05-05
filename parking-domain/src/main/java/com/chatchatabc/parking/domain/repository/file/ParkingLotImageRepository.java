package com.chatchatabc.parking.domain.repository.file;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotImageRepository extends JpaRepository<ParkingLotImage, String> {

    /**
     * Find all parking lot images by parking lot and bit value
     *
     * @param parkingLot the parking lot
     * @param divisor    the divisor of the flag
     * @param bitValue   the bit value of the flag
     * @param pageable   the pageable
     * @return the page of parking lot images
     */
    @Query("SELECT pli FROM ParkingLotImage pli WHERE pli.parkingLot = :parkingLot AND MOD(pli.flag / :divisor, 2) = :bitValue")
    Page<ParkingLotImage> findAllByParkingLotAndFlag(ParkingLot parkingLot, int divisor, int bitValue, Pageable pageable);
}
