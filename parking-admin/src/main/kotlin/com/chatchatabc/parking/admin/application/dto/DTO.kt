package com.chatchatabc.parking.admin.application.dto

import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import java.math.BigDecimal
import java.time.LocalDateTime

data class ApiResponse<T>(
    val data: T?,
    val code: Int = 0,
    val message: String?,
    val error: Boolean = false
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

data class DashboardStatistics(
    val totalMembers: Long?,
    val totalVerifiedMembers: Long?,
    val totalUnverifiedMembers: Long?,
    val totalVehicles: Long?,
    val totalParkingLots: Long?,
    val totalVerifiedParkingLots: Long?,
    val totalUnverifiedParkingLots: Long?,
    val totalActiveInvoices: Long?
)

data class ParkingLotCreateRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val description: String,
    val capacity: Int,
    val businessHoursStart: LocalDateTime?,
    val businessHoursEnd: LocalDateTime?,
    val openDaysFlag: Int = 0
)

data class ParkingLotUpdateRequest(
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val description: String?,
    val capacity: Int?,
    val availableSlots: Int?,
    val businessHoursStart: LocalDateTime?,
    val businessHoursEnd: LocalDateTime?,
    val openDaysFlag: Int?,
    val images: List<ParkingLotImage>?
)

data class RateUpdateRequest(
    val type: Int?,
    val interval: Int?,
    val freeHours: Int?,
    val payForFreeHoursWhenExceeding: Boolean?,
    val startingRate: BigDecimal?,
    val rate: BigDecimal?
)