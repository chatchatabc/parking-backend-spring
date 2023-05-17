package com.chatchatabc.parking.api.application.dto

import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import java.math.BigDecimal
import java.time.LocalDateTime

data class ApiResponse<T>(
    val data: T? = null,
    val code: Int = 0,
    val message: String?,
    val error: Boolean = false
)

data class MemberPhoneLoginRequest(
    val phone: String,
    val username: String?
)

data class MemberVerifyOTPRequest(
    val phone: String,
    val otp: String
)

data class MemberNotificationResponse(
    val notificationId: String
)

data class VehicleRegisterRequest(
    val name: String,
    val plateNumber: String,
    val type: Int,
)

data class ReportCreateRequest(
    val name: String,
    val description: String,
    val plateNumber: String,
    val latitude: Double,
    val longitude: Double,
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