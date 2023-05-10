package com.chatchatabc.parking.admin.application.dto.member

import java.time.LocalDateTime

data class MemberLoginRequest(
    val username: String,
    val password: String
)

data class MemberCreateRequest(
    val phone: String,
    val username: String?,
    val email: String?,
    val roles: List<String>,
    val enabled: Boolean = true
)

data class MemberUpdateRequest(
    val email: String?,
    val phone: String?,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
)

data class MemberOverridePasswordRequest(
    val newPassword: String,
)

data class MemberBanRequest(
    val until: LocalDateTime,
    val reason: String
)

data class MemberUnbanRequest(
    val unbanReason: String
)