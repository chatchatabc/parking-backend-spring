package com.chatchatabc.service.domain.repository

import com.chatchatabc.service.domain.model.ParkingRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ParkingRoleRepository : JpaRepository<ParkingRole, String> {
    /**
     * Find role by name
     */
    fun findByName(name: String): Optional<ParkingRole>
}