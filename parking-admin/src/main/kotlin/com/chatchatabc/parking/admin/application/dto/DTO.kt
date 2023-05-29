package com.chatchatabc.parking.admin.application.dto

import java.time.LocalDateTime

data class ApiResponse<T>(
    val data: T? = null,
    val errors: List<ErrorElement>? = null
)

data class ErrorElement(
    val title: String?,
    val message: String?
)

data class PagedResponse<T>(
    val content: List<T>,
    val pageInfo: PageInfo
)

data class PageInfo(
    val size: Int,
    val totalElements: Long,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)

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