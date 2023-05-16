package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.ParkingLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long>, JpaSpecificationExecutor<ParkingLot> {
    /**
     * Find Parking Lot by parking lot uuid
     *
     * @param parkingLotUuid the parking lot uuid
     * @return the parking lot
     */
    Optional<ParkingLot> findByParkingLotUuid(String parkingLotUuid);

    /**
     * Find parking lot by owner
     *
     * @param owner the owner id
     * @return parking lot
     */
    Optional<ParkingLot> findByOwner(Long owner);


    /**
     * Find parking lots by distance using Haversine formula
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.status > 1 AND (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) <= :distance")
    List<ParkingLot> findByDistance(double latitude, double longitude, double distance);

    /**
     * Find all parking lots by owner
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.owner = ?1")
    Page<ParkingLot> findAllByOwner(Member owner, Pageable pageable);

    /**
     * Find all parking lots by owner and status value
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.owner = :owner AND p.status = :status")
    Page<ParkingLot> findAllByOwnerAndStatus(Member owner, int status, Pageable pageable);

    /**
     * Count verified parking lots
     *
     * @return the count
     */
    @Query("SELECT COUNT(p.id) FROM ParkingLot p WHERE p.verifiedAt IS NOT NULL")
    Long countVerified();
}
