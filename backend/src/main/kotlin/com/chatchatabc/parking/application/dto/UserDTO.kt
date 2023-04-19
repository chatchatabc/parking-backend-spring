package com.chatchatabc.parking.application.dto

import com.chatchatabc.parking.domain.model.User

data class UserRegisterRequest(
    val email: String,
    val username: String,
    val password: String
)

data class UserLoginRequest(
    val username: String,
    val password: String
)

data class UserParkingPhoneLoginRequest(
    val phone: String
)

data class UserParkingPhoneLoginResponse(
    val isRegistered: Boolean?,
    val error: ErrorContent?
)

data class UserParkingVerifyRequest(
    val phone: String,
    val otp: String
)

data class UserResponse(
    val user: User?,
    val error: ErrorContent?
)
