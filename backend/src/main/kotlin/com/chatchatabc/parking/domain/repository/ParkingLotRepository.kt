package com.chatchatabc.parking.domain.repository

import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ParkingLotRepository : JpaRepository<ParkingLot, String> {

    /**
     * Find a parking lot by its id and owner
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.id = ?1 AND p.owner = ?2")
    fun findByIdAndOwner(parkingLotId: String, owner: User): Optional<ParkingLot>

    /**
     * Find parking lots by distance using Haversine formula
     */
    @Query("SELECT p FROM ParkingLot p WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) <= :distance")
    fun findByDistance(longitude: Double, latitude: Double, distance: Double): List<ParkingLot>
}