package com.chatchatabc.parking.api.application.dto

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