package com.chatchatabc.parking.admin.application.dto

import java.time.LocalDateTime

data class UserLoginRequest(
    val username: String,
    val password: String
)

data class UserOverridePasswordRequest(
    val newPassword: String,
)

data class UserBanRequest(
    val until: LocalDateTime,
    val reason: String
)

data class UserUnbanRequest(
    val unbanReason: String
)

data class DashboardStatistics(
    val totalUsers: Long?,
    val totalVerifiedUsers: Long?,
    val totalUnverifiedUsers: Long?,
    val totalVehicles: Long?,
    val totalParkingLots: Long?,
    val totalVerifiedParkingLots: Long?,
    val totalUnverifiedParkingLots: Long?,
    val totalActiveInvoices: Long?
)