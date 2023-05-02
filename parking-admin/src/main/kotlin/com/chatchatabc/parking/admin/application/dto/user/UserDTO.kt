package com.chatchatabc.parking.admin.application.dto.user

data class UserLoginRequest(
        val username: String,
        val password: String
)

data class UserCreateRequest(
        val phone: String,
        val username: String?
)