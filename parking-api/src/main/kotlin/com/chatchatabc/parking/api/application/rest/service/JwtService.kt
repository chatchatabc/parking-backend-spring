package com.chatchatabc.parking.api.application.rest.service

import com.chatchatabc.parking.domain.model.User

interface JwtService {

    /**
     * Generate a JWT token for the given user id
     */
    fun generateToken(userId: String): String

    /**
     * Validate the given token and return the user
     */
    fun validateTokenAndGetUser(token: String): User
}
