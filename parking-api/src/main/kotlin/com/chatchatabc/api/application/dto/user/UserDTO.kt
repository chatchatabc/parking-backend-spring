package com.chatchatabc.api.application.dto.user

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.parking.domain.model.User

data class UserResponse(
    val user: User?,
    val error: ErrorContent?
)

data class UserPhoneLoginRequest(
    val phone: String,
    val username: String?
)

data class UserPhoneLoginResponse(
    val error: ErrorContent?
)

data class UserProfileUpdateRequest(
    val username: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?
)

data class UserVerifyOTPRequest(
    val phone: String,
    val otp: String
)