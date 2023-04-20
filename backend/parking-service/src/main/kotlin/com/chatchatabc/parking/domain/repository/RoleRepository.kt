package com.chatchatabc.parking.domain.repository

import com.chatchatabc.parking.domain.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, String> {

    /**
     * Find a role by name
     */
    fun findByName(name: String): Optional<Role>
}