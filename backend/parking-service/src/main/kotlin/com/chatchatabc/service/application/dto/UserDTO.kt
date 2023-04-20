package com.chatchatabc.service.application.dto

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.application.dto.user.UserDTO

data class UserPhoneLoginRequest(
    val username: String?,
    val phone: String
)

data class UserPhoneLoginResponse(
    val error: ErrorContent?
)

data class UserVerifyOTPRequest(
    val phone: String,
    val otp: String
)

data class UserProfileUpdateRequest(
    val username: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?
)

data class UserResponse(
    val user: UserDTO?,
    val error: ErrorContent?
)
