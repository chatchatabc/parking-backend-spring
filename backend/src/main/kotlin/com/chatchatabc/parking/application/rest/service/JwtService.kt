package com.chatchatabc.parking.application.rest.service

import com.chatchatabc.parking.domain.model.User
import org.springframework.stereotype.Service

@Service
interface JwtService {
    /**
     * Generate Token
     */
    fun generateToken(user: User): String

    /**
     * Validate Token and Get User
     */
    fun validateTokenAndGetUser(token: String): User
}