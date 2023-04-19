package com.chatchatabc.parking.domain.repository

import com.chatchatabc.parking.domain.model.ParkingRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ParkingRoleRepository : JpaRepository<ParkingRole, String> {
    /**
     * Find role by name
     */
    fun findByName(name: String): Optional<ParkingRole>
}