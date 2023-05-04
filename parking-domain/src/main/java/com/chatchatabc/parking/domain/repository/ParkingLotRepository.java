package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {
    /**
     * Find a parking lot by its id and owner
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.id = ?1 AND p.owner = ?2")
    Optional<ParkingLot> findByIdAndOwner(String id, User owner);

    /**
     * Find parking lots by distance using Haversine formula
     */
    @Query("SELECT p FROM ParkingLot p WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) <= :distance")
    List<ParkingLot> findByDistance(double latitude, double longitude, double distance);

    /**
     * Find all parking lots by owner
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.owner = ?1")
    Page<ParkingLot> findAllByOwner(User owner, Pageable pageable);

    /**
     * Find all parking lots by owner and flag bit
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.owner = :owner AND MOD(p.flag / :divisor, 2) = :bitValue")
    Page<ParkingLot> findAllByOwnerAndFlag(User owner, int divisor, int bitValue, Pageable pageable);

}
