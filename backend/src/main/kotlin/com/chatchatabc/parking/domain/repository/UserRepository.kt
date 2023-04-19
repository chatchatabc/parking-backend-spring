package com.chatchatabc.parking.domain.repository

import com.chatchatabc.parking.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {

    /**
     * Find a user by username
     */
    fun findByUsername(username: String): Optional<User>

    /**
     * Find a user by phone
     */
    fun findByPhone(phone: String): Optional<User>
}