package com.chatchatabc.parking.application.dto

import com.chatchatabc.parking.domain.model.User

data class UserRegisterRequest(
    val email: String,
    val username: String,
    val password: String
)

data class UserResponse(
    val user: User?,
    val error: ErrorContent?
)
