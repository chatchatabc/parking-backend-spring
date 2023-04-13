package com.chatchatabc.parking.domain.repository

import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.Vehicle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VehicleRepository : JpaRepository<Vehicle, String> {
    /**
     * Find a vehicle by its id and user
     */
    @Query("SELECT v FROM Vehicle v WHERE v.id = ?1 AND v.user = ?2")
    fun findByIdAndUser(vehicleId: String, user: User): Optional<Vehicle>
}