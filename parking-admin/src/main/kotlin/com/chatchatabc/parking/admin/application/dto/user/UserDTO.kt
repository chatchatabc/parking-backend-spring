package com.chatchatabc.parking.admin.application.dto.user

data class UserLoginRequest(
        val username: String,
        val password: String
)

data class UserCreateRequest(
        val phone: String,
        val username: String?
)

data class UserUpdateRequest(
        val email: String?,
        val phone: String?,
        val username: String?,
        val firstName: String?,
        val lastName: String?,
)