package com.chatchatabc.service.domain.repository

import com.chatchatabc.service.domain.model.User
import com.chatchatabc.service.domain.model.Vehicle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VehicleRepository : JpaRepository<Vehicle, String> {
    /**
     * Find a vehicle by its id and user
     */
    @Query("SELECT v FROM Vehicle v WHERE v.id = ?1 AND v.owner = ?2")
    fun findByIdAndOwner(vehicleId: String, owner: User): Optional<Vehicle>

    /**
     * Find all vehicles of a user
     */
    @Query("SELECT v FROM Vehicle v WHERE v.owner = ?1")
    fun findAllByOwner(owner: User, pageable: Pageable): Page<Vehicle>
}