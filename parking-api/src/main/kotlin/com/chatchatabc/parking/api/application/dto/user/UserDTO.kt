package com.chatchatabc.parking.api.application.dto.user

data class UserPhoneLoginRequest(
    val phone: String,
    val username: String?
)

data class UserProfileUpdateRequest(
    val username: String?,
    val phone: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?
)

data class UserVerifyOTPRequest(
    val phone: String,
    val otp: String
)